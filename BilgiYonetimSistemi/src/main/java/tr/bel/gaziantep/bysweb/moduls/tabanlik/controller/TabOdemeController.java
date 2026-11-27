package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepIslem;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabUcretTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabBelge;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabOdeme;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabBelgeService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabBelgeZorunlulukService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabOdemeService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Ücretlendirme, ödeme ve onay ekranı.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Named
@ViewScoped
@Slf4j
public class TabOdemeController extends AbstractController<TabOdeme> {

    @Serial
    private static final long serialVersionUID = -9018273654019382751L;

    @Inject
    private TabOdemeService service;
    @Inject
    private TabBelgeService belgeService;
    @Inject
    private TabBelgeZorunlulukService zorunlulukService;
    @Inject
    private TabTalepWorkflowService workflowService;

    @Getter
    @Setter
    private TabTalep seciliTalep;
    @Getter
    @Setter
    private EnumTabUcretTuru ucretTuru;
    @Getter
    @Setter
    private BigDecimal tutar;
    @Getter
    @Setter
    private LocalDateTime odemeTarihi = LocalDateTime.now();
    @Getter
    @Setter
    private String odemeYontemi;
    @Getter
    @Setter
    private String dekontNo;
    @Getter
    @Setter
    private String ucretsizGerekce;
    @Getter
    @Setter
    private String aciklama;

    @Getter
    @Setter
    private EnumTabBelgeTuru belgeTuru;
    @Getter
    @Setter
    private String belgeAciklama;

    @Getter
    private List<TabOdeme> odemeler = Collections.emptyList();
    @Getter
    private List<TabBelge> belgeler = Collections.emptyList();

    @Getter
    @Setter
    private StreamedContent download;

    public TabOdemeController() {
        super(TabOdeme.class);
    }

    public void onTalepSelected(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            this.seciliTalep = event.getObject();
            this.ucretTuru = seciliTalep.getUcretTuru();
            this.odemeTarihi = LocalDateTime.now();
            this.tutar = null;
            this.dekontNo = null;
            this.ucretsizGerekce = null;
            this.aciklama = null;
            refreshLists();
        }
    }

    public void saveCharge() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("İşlem için talep seçilmelidir.");
            return;
        }
        try {
            workflowService.saveCharge(seciliTalep.getId(), ucretTuru, tutar, odemeTarihi,
                    odemeYontemi, dekontNo, ucretsizGerekce, aciklama);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            refreshLists();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void approveCharge() {
        if (seciliTalep == null) {
            FacesUtil.addErrorMessage("Onay için talep seçilmelidir.");
            return;
        }
        TabOdeme bekleyen = service.findByTalep(seciliTalep).stream()
                .filter(o -> o.getOnayDurumu() == tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabOnayDurumu.BEKLIYOR)
                .findFirst()
                .orElse(null);
        if (bekleyen == null) {
            FacesUtil.addErrorMessage("Onay bekleyen ödeme kaydı bulunamadı.");
            return;
        }
        try {
            workflowService.approveCharge(bekleyen.getId(), aciklama);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            refreshLists();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    public void upload(FileUploadEvent event) {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Belge yüklemek için talep seçilmelidir.");
            return;
        }
        try {
            belgeService.upload(seciliTalep, belgeTuru, event.getFile(), belgeAciklama);
            belgeAciklama = null;
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            refreshLists();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Seçili ücret türüne göre onayda zorunlu belge türleri (DB'den yönetilir).
     */
    public List<EnumTabBelgeTuru> getRequiredBelgeler() {
        if (ucretTuru == null) {
            return List.of();
        }
        EnumTabTalepIslem islem = ucretTuru == EnumTabUcretTuru.UCRETLI
                ? EnumTabTalepIslem.UCRETLI_ODEME_ONAYLA
                : EnumTabTalepIslem.UCRETSIZ_ONAYLA;
        return zorunlulukService.getRequiredTurler(ucretTuru, islem.name());
    }

    public void selectForDownload(TabBelge belge) {
        if (belge == null || belge.getDosyaYolu() == null) {
            download = null;
            return;
        }
        download = DefaultStreamedContent.builder()
                .name(belge.getDosyaAdi())
                .contentType(belge.getMimeTuru() == null ? "application/octet-stream" : belge.getMimeTuru())
                .stream(() -> {
                    try {
                        return new FileInputStream(belge.getDosyaYolu());
                    } catch (IOException ex) {
                        log.error("Belge okunamadı: " + belge.getDosyaYolu(), ex);
                        return null;
                    }
                })
                .build();
    }

    private void refreshLists() {
        odemeler = seciliTalep == null ? Collections.emptyList() : service.findByTalep(seciliTalep);
        belgeler = seciliTalep == null ? Collections.emptyList() : belgeService.findByTalep(seciliTalep);
    }
}
