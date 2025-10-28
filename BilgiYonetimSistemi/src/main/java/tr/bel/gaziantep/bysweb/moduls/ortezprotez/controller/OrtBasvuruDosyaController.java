package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

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
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruDosya;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtBasvuruDosyaService;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.10.2025 08:57
 */
@Named
@ViewScoped
@Slf4j
public class OrtBasvuruDosyaController extends AbstractController<OrtBasvuruDosya> {

    @Serial
    private static final long serialVersionUID = -7389593499242629237L;

    @Inject
    private OrtBasvuruDosyaService basvuruDosyaService;

    @Getter
    @Setter
    private OrtBasvuru ortBasvuru;
    @Getter
    @Setter
    private OrtBasvuruDosya selectedDosya;
    @Setter
    private StreamedContent download;
//    private StreamedContent onizle;

    public OrtBasvuruDosyaController() {
        super(OrtBasvuruDosya.class);
    }

    public ActionListener getInfo() {
        return event -> ortBasvuru = (OrtBasvuru) event.getComponent().getAttributes().get("ortBasvuru");
    }

    public StreamedContent getDownload() {
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

//    public StreamedContent getOnizle() throws FileNotFoundException {
//        if (selectedDosya != null) {
//            File file = new File(selectedDosya.getDosyaYolu());
//            InputStream stream = new FileInputStream(file);
//            onizle = DefaultStreamedContent.builder().contentType("image/png").name(selectedDosya.getDosyaAdi()).stream(() -> stream).build();
//        }
//        return onizle;
//    }

    public void handleFileUpload(FileUploadEvent event) {
        try {

            String reportPath = System.getProperty("os.name").equals("Mac OS X") ? Constants.ORTEZ_PROTEZ_RAPOR_RECETE_KLASORU_MAC : Constants.ORTEZ_PROTEZ_RAPOR_RECETE_KLASORU;

            File tcFolder = new File(reportPath + ortBasvuru.getOrtHasta().getGnlKisi().getTcKimlikNo());

            if (!tcFolder.exists()) {
                tcFolder.mkdir();
            }

            String dosyaAdi = ortBasvuru.getOrtHasta().getGnlKisi().getTcKimlikNo() + "_" + "_" + DateUtil.localdateTimeToString(LocalDateTime.now(), "dd_MM_yyyy_HH_mm_ss_SSS")
                    + Function.getExtension(event.getFile());

            String fileName = tcFolder + File.separator + dosyaAdi;

            File result = new File(fileName);

            FileOutputStream fileOutputStream = new FileOutputStream(result);


            byte[] buffer = new byte[8192];

            int bulk;

            List<String> contentType = Arrays.asList("png", "jpg", "jpeg", "bmp");

            InputStream inputStream = contentType.contains(event.getFile().getContentType()) ? ImageUtil.compressImage(event.getFile().getInputStream(), result) : event.getFile().getInputStream();

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

            OrtBasvuruDosya kisiDosya = new OrtBasvuruDosya();
            kisiDosya.setDosyaAdi(dosyaAdi);
            kisiDosya.setDosyaYolu(fileName);
            kisiDosya.setOrtBasvuru(ortBasvuru);
            basvuruDosyaService.create(kisiDosya);
            ortBasvuru.getOrtBasvuruDosyaList().add(kisiDosya);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("dosyaYuklenirkenHataOlustu");
        }

    }

    public void deleteFile(OrtBasvuruDosya dosya) {
        if (dosya != null) {
            try {
                File file = new File(dosya.getDosyaYolu());
//                if (file.delete()) {
//                    basvuruDosyaService.remove(dosya);
//                    FacesUtil.successMessage("kayitSilindi");
//                } else {
//                    FacesUtil.warningMessage("dosyaSilinemedi");
//                }
                if (file.exists()) {
                    basvuruDosyaService.removeSlient(dosya);
                    FacesUtil.successMessage("kayitSilindi");
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

    public StreamedContent getContentPdf() {
        if(selectedDosya==null){
            InputStream emptyPdf = Util.getServletContext().getResourceAsStream("/resources/pdf/emptypdf.pdf");
            return DefaultStreamedContent.builder()
                    .contentType("application/pdf")
                    .name("emptypdf")
                    .stream(()->emptyPdf)
                    .build();
        }
        return DefaultStreamedContent.builder()
                .contentType("application/pdf")
                .name(selectedDosya.getDosyaAdi())
                .stream(() -> {
                    try {
                        return new FileInputStream(selectedDosya.getDosyaYolu());
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }
}
