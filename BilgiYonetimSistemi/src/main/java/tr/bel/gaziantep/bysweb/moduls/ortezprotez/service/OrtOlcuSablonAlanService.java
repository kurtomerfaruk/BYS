package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtSablonAlanTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablonAlan;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 09:53
 */
@Stateless
public class OrtOlcuSablonAlanService extends AbstractService<OrtOlcuSablonAlan> {

    @Serial
    private static final long serialVersionUID = 7239039020935244638L;

    public OrtOlcuSablonAlanService() {
        super(OrtOlcuSablonAlan.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    static {
        nu.pattern.OpenCV.loadLocally();
    }

    public List<OrtOlcuSablonAlan> findByOrtOlcuSablonId(int ortOlcuSablonId) {
        return getEntityManager().createNamedQuery("OrtOlcuSablonAlan.findByOrtOlcuSablonId")
                .setParameter("ortOlcuSablonId",ortOlcuSablonId)
                .getResultList();
    }

    public List<OrtOlcuSablonAlan> detectAndSaveFields(OrtOlcuSablon template, String imagePath) {
        Mat img = Imgcodecs.imread(imagePath);
        if (img.empty()) {
            throw new RuntimeException("Resim okunamadı: " + imagePath);
        }

        int imageWidth = img.width();
        int imageHeight = img.height();

        template.setResimGenislik(imageWidth);
        template.setResimYukseklik(imageHeight);

        // Griye çevir
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

        // Kenar tespiti
        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);

        // Kontur bulma
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        List<OrtOlcuSablonAlan> detected = new ArrayList<>();
        int counter = 1;

        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);

            // Filtreleme (çok küçük/büyük kutuları alma)
            if (rect.width > 25 && rect.height > 15 && rect.width < imageWidth - 50 && rect.height < imageHeight - 50) {
                OrtOlcuSablonAlan field = new OrtOlcuSablonAlan();
                if (rect.width < 40 && rect.height < 40) {
                    field.setTur(EnumOrtSablonAlanTuru.CHECKBOX);
                } else  {
                    field.setTur(EnumOrtSablonAlanTuru.TEXT);
                }

                field.setOrtOlcuSablon(template);
                field.setTanim("kutucuk_" + counter++);
                field.setX(rect.x);
                field.setY(rect.y);
                field.setGenislik(rect.width);
                field.setYukseklik(rect.height);

                em.persist(field);
                detected.add(field);
            }
        }


        getEntityManager().merge(template);
        return detected;
    }


    private MatOfPoint hullPoints(MatOfInt hullInt, MatOfPoint contour) {
        Point[] contourPts = contour.toArray();
        int[] hullIndices = hullInt.toArray();
        Point[] hullPts = new Point[hullIndices.length];
        for (int i = 0; i < hullIndices.length; i++) {
            hullPts[i] = contourPts[hullIndices[i]];
        }
        MatOfPoint mop = new MatOfPoint();
        mop.fromArray(hullPts);
        return mop;
    }
}
