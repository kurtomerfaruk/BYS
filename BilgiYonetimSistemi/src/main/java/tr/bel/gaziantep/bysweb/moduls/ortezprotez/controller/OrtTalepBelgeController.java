package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
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
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepIslem;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepBelge;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepOdeme;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepBelgeService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepOdemeService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 25.11.2026 14:14
 */
@Named
@ViewScoped
@Slf4j
public class OrtTalepBelgeController extends AbstractController<OrtTalepBelge> {

    @Serial
    private static final long serialVersionUID = 5483224678185024366L;

    @Inject
    private OrtTalepBelgeService service;
    @Inject
    private OrtTalepOdemeService odemeService;
    @Inject
    private OrtTalepController ortTalepController;

    @Getter
    @Setter
    private EnumOrtBelgeTuru seciliBelgeTuru;
    @Getter
    @Setter
    private String belgeAciklama;
    @Getter
    @Setter
    private BigDecimal odemeTutar;
    @Getter
    @Setter
    private String odemeSekli;
    @Getter
    @Setter
    private String dekontNo;
    @Getter
    @Setter
    private OrtTalepBelge selectedBelge;
    @Setter
    private StreamedContent download;

    @Getter
    private List<OrtTalepBelge> belgeler = Collections.emptyList();
    @Getter
    private List<OrtTalepOdeme> odemeler = Collections.emptyList();

    public OrtTalepBelgeController() {
        super(OrtTalepBelge.class);
    }

    private OrtTalep currentTalep() {
        return ortTalepController == null ? null : ortTalepController.getSelected();
    }

    public void loadForTalep() {
        OrtTalep talep = currentTalep();
        belgeler = service.findByTalep(talep);
        odemeler = odemeService.findByTalep(talep);
    }

    /**
     * "Sıradaki İşlem" kartında gösterilecek belge türü. İşlem, belge yükleme
     * gerektiriyorsa ilgili tür döner; aksi halde null.
     */
    public EnumOrtBelgeTuru getRequiredBelgeTuru() {
        EnumOrtTalepIslem action = ortTalepController == null ? null : ortTalepController.getPrimaryAction();
        if (action == null) {
            return null;
        }
        return switch (action) {
            case FORMLARI_TAMAMLA -> EnumOrtBelgeTuru.OLCU_FORMU;
            case RAPOR_AL -> EnumOrtBelgeTuru.RAPOR;
            case RECETE_AL -> EnumOrtBelgeTuru.RECETE;
            case SOZLESME_IMZALA -> EnumOrtBelgeTuru.SOZLESME;
            case ODEME_TAMAMLA -> EnumOrtBelgeTuru.DEKONT;
            case TESLIM_ET, TAMAMLA -> EnumOrtBelgeTuru.TESLIM_TUTANAGI;
            default -> null;
        };
    }

    /**
     * Gerekli belge türü için "Sıradaki İşlem" kartından otomatik yükleme.
     */
    public void uploadRequired(FileUploadEvent event) {
        EnumOrtBelgeTuru tur = getRequiredBelgeTuru();
        if (tur == null) {
            FacesUtil.addErrorMessage("Bu işlem için belge yüklemesi gerekmiyor.");
            return;
        }
        OrtTalep talep = currentTalep();
        if (talep == null || talep.getId() == null) {
            FacesUtil.addErrorMessage("Talep seçilmeden belge yüklenemez.");
            return;
        }
        try {
            service.upload(talep, tur, event.getFile(), "İşlem: " + ortTalepController.getPrimaryAction().getDisplayValue());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            loadForTalep();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Gerekli belge bu talep için yüklenmiş mi?
     */
    public boolean isRequiredBelgeYuklendi() {
        EnumOrtBelgeTuru tur = getRequiredBelgeTuru();
        return tur != null && service.existsByType(currentTalep(), tur);
    }

    public void upload(FileUploadEvent event) {
        OrtTalep talep = currentTalep();
        if (talep == null || talep.getId() == null) {
            FacesUtil.addErrorMessage("Talep seçilmeden belge yüklenemez.");
            return;
        }
        try {
            service.upload(talep, seciliBelgeTuru, event.getFile(), belgeAciklama);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            belgeAciklama = null;
            loadForTalep();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void removeBelge(OrtTalepBelge belge) {
        try {
            service.deleteDocument(belge);
            FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            loadForTalep();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void selectForDownload(OrtTalepBelge belge) {
        this.selectedBelge = belge;
    }

    public StreamedContent getDownload() {
        if (selectedBelge == null || selectedBelge.getDosyaYolu() == null) {
            return DefaultStreamedContent.builder().build();
        }
        final String dosyaYolu = selectedBelge.getDosyaYolu();
        String type = selectedBelge.getMimeType() == null ? "application/octet-stream" : selectedBelge.getMimeType();
        download = DefaultStreamedContent.builder()
                .name(selectedBelge.getDosyaAdi())
                .contentType(type)
                .stream(() -> {
                    try {
                        return new FileInputStream(dosyaYolu);
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                })
                .build();
        return download;
    }

    public void saveOdeme(ActionEvent event) {
        OrtTalep talep = currentTalep();
        if (talep == null || talep.getId() == null) {
            FacesUtil.addErrorMessage("Talep seçilmeden ödeme kaydedilemez.");
            return;
        }
        if (odemeTutar == null || odemeTutar.compareTo(BigDecimal.ZERO) <= 0) {
            FacesUtil.addErrorMessage("Ödeme tutarı sıfırdan büyük olmalıdır.");
            return;
        }
        try {
            OrtTalepOdeme odeme = OrtTalepOdeme.builder()
                    .ortTalep(talep)
                    .tutar(odemeTutar)
                    .odemeTarihi(LocalDateTime.now())
                    .odemeSekli(odemeSekli)
                    .dekontNo(dekontNo)
                    .build();
            odemeService.create(odeme);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            odemeTutar = null;
            odemeSekli = null;
            dekontNo = null;
            loadForTalep();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public boolean hasBelge(EnumOrtBelgeTuru tur) {
        return service.existsByType(currentTalep(), tur);
    }
}
