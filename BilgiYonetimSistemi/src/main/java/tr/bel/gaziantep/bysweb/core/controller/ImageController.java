package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.extensions.event.RotateEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiDosya;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruDosya;

import java.io.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 14:46
 */
@Named
@ApplicationScoped
public class ImageController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -5600348110678215309L;

    private StreamedContent preview;

    public StreamedContent getPreview() {
        if(preview == null) {
            preview = DefaultStreamedContent.builder().build();
        }
        return preview;
    }

    public void setPreview(StreamedContent preview) {
        this.preview = preview;
    }

    public void rotateListener(final RotateEvent e) {
        final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Image rotated",
                "Degree:" + e.getDegree());

        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public StreamedContent getKisiDosyaImage(EyKisiDosya dosya) throws FileNotFoundException {
        if (dosya != null) {
            File file = new File(dosya.getDosyaYolu());
            InputStream stream = new FileInputStream(file);
            preview = DefaultStreamedContent.builder().contentType("image/png").name(dosya.getDosyaAdi()).stream(() -> stream).build();

//            try {
//                Path path = Paths.get(dosya.getDosyaYolu());
//                byte[] imageBytes = Files.readAllBytes(path);
//                preview = DefaultStreamedContent.builder()
//                        .contentType(getContentType(dosya.getDosyaAdi()))
//                        .name(dosya.getDosyaAdi())
//                        .stream(() -> new ByteArrayInputStream(imageBytes))
//                        .build();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return preview;
    }

    private String getContentType(String fileName) {
        if (fileName == null) return "application/octet-stream";

        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
        if (fileName.endsWith(".gif")) return "image/gif";
        if (fileName.endsWith(".pdf")) return "application/pdf";
        return "application/octet-stream";
    }

    public StreamedContent getOrtBasvuruDosyaImage(OrtBasvuruDosya dosya) throws FileNotFoundException {
        if (dosya != null) {
            File file = new File(dosya.getDosyaYolu());
            InputStream stream = new FileInputStream(file);
            preview = DefaultStreamedContent.builder().contentType("image/png").name(dosya.getDosyaAdi()).stream(() -> stream).build();
        }
        return preview;
    }
}
