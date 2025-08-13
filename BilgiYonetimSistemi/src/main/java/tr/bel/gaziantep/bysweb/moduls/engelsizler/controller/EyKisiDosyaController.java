package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ActionListener;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiDosya;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiDosyaService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:10
 */
@Named
@ViewScoped
@Slf4j
public class EyKisiDosyaController extends AbstractController<EyKisiDosya> {
    @Serial
    private static final long serialVersionUID = 6118108219016674026L;

    @Inject
    private EyKisiDosyaService eyKisiDosyaService;

    @Getter
    @Setter
    private EyKisi eyKisi;
    @Getter
    @Setter
    private EyKisiDosya selectedDosya;
    @Setter
    private StreamedContent download;
    private StreamedContent onizle;

    public EyKisiDosyaController() {
        super(EyKisiDosya.class);
    }

    public ActionListener kisiBilgisiGetir() {
        return new ActionListener() {
            @Override
            public void processAction(ActionEvent event) throws AbortProcessingException {
                eyKisi = (EyKisi) event.getComponent().getAttributes().get("eyKisi");
            }
        };
    }

    public StreamedContent getDownload() throws FileNotFoundException {
        download = DefaultStreamedContent.builder()
                .contentType("image/png")
                .name(selectedDosya.getDosyaAdi())
                .stream(() -> {
                    try {
                        return new FileInputStream(selectedDosya.getDosyaYolu());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
        return download;
    }

    public StreamedContent getOnizle() throws FileNotFoundException {
        if (selectedDosya != null) {
            File file = new File(selectedDosya.getDosyaYolu());
            InputStream stream = new FileInputStream(file);
            onizle = DefaultStreamedContent.builder().contentType("image/png").name(selectedDosya.getDosyaAdi()).stream(() -> stream).build();
        }
        return onizle;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            String reportPath = System.getProperty("os.name").equals("Mac OS X") ? Constants.ENGELSIZLER_KISI_DOSYALARI_MAC : Constants.ENGELSIZLER_KISI_DOSYALARI;

            File tcFolder = new File(reportPath + eyKisi.getGnlKisi().getTcKimlikNo());

            if (!tcFolder.exists()) {
                tcFolder.mkdir();
            }

            String dosyaAdi = eyKisi.getGnlKisi().getTcKimlikNo() + "_" + "_" + DateUtil.localdateTimeToString(LocalDateTime.now(), "dd_MM_yyyy_HH_mm_ss_SSS")
                    + Function.getExtension(event.getFile());

            String fileName = tcFolder + File.separator + dosyaAdi;

            File result = new File(fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(result);


            byte[] buffer = new byte[8192];

            int bulk;

            List<String> contentType = Arrays.asList("png", "jpg", "jpeg", "bmp");

            InputStream inputStream = contentType.contains(event.getFile().getContentType()) ? compressImage(event.getFile().getInputStream(), result) : event.getFile().getInputStream();

            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

            EyKisiDosya kisiDosya = new EyKisiDosya();
            kisiDosya.setDosyaAdi(dosyaAdi);
            kisiDosya.setDosyaYolu(fileName);
            kisiDosya.setEyKisi(eyKisi);
            kisiDosya.setTarih(LocalDateTime.now());
            eyKisiDosyaService.create(kisiDosya);
            eyKisi.getEyKisiDosyaList().add(kisiDosya);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("dosyaYuklenirkenHataOlustu");
        }

    }

    private InputStream compressImage(InputStream inputStream, File file) {
        try (InputStream is = inputStream;
             OutputStream out = new FileOutputStream(file)) {

            BufferedImage img = ImageIO.read(is);
            BufferedImage scaledImg;
            if (img.getWidth() >= img.getHeight())
                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, 800, 1000);
            else
                scaledImg = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, 1000, 800);
            ImageIO.write(scaledImg, "jpg", out);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(scaledImg, "jpg", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void dosyaSil(EyKisiDosya dosya) {
        if (dosya != null) {
            try {
                File file = new File(dosya.getDosyaYolu());
                if (file.delete()) {
                    eyKisiDosyaService.remove(dosya);
                    FacesUtil.successMessage("kayitSilindi");
                } else {
                    FacesUtil.warningMessage("dosyaSilinemedi");
                }

            } catch (Exception e) {
                try {
                    throw new Exception(FacesUtil.message("dosyaSilinemedi"));
                } catch (Exception ex) {
                    log.error(null, ex);
                }
            }
        }
    }
}
