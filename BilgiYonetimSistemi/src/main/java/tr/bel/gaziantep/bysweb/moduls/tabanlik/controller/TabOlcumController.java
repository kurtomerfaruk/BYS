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
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabAyakTarafi;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepIslem;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabBelge;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabOlcum;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabOlcumDetay;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabBelgeService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabOlcumService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 14:39
 */
@Named
@ViewScoped
@Slf4j
public class TabOlcumController extends AbstractController<TabOlcum> {

    @Serial
    private static final long serialVersionUID = 701863325449861142L;

    @Inject
    private TabOlcumService service;
    @Inject
    private TabBelgeService belgeService;
    @Inject
    private TabTalepWorkflowService workflowService;

    @Getter
    private final TabOlcumDetay sagDetay = TabOlcumDetay.builder().ayakTarafi(EnumTabAyakTarafi.SAG).build();
    @Getter
    private final TabOlcumDetay solDetay = TabOlcumDetay.builder().ayakTarafi(EnumTabAyakTarafi.SOL).build();

    @Getter
    @Setter
    private TabTalep seciliTalep;
    @Getter
    @Setter
    private LocalDateTime olcumTarihi = LocalDateTime.now();
    @Getter
    @Setter
    private String aciklama;
    @Getter
    @Setter
    private String belgeAciklama;

    @Getter
    private List<TabOlcum> olcumler = Collections.emptyList();
    @Getter
    private List<TabBelge> belgeler = Collections.emptyList();

    @Getter
    @Setter
    private StreamedContent download;

    public TabOlcumController() {
        super(TabOlcum.class);
    }

    /**
     * Talep seçim dialogundan dönen talep ile formu hazırlar.
     */
    public void onTalepSelected(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            prepareForTalep(event.getObject());
        }
    }

    public void prepareForTalep(TabTalep talep) {
        this.seciliTalep = talep;
        this.olcumTarihi = LocalDateTime.now();
        this.aciklama = null;
        refreshLists();
    }

    /**
     * Ölçüm kaydını (sağ/sol detaylarla) yazar ve talebi ölçüm aşamasına geçirir.
     */
    public void saveMeasurement() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Ölçüm için talep seçilmelidir.");
            return;
        }
        try {
            TabOlcum olcum = TabOlcum.builder()
                    .tabTalep(seciliTalep)
                    .olcumTarihi(olcumTarihi)
                    .aciklama(aciklama)
                    .detayList(new ArrayList<>(List.of(sagDetay, solDetay)))
                    .build();
            workflowService.createOlcumAndTransition(olcum);
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

    /**
     * Ölçümü tamamlar ve talebi üretim kararı aşamasına taşır.
     */
    public void completeMeasurement() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Ölçüm için talep seçilmelidir.");
            return;
        }
        try {
            workflowService.applyTransition(seciliTalep.getId(), EnumTabTalepIslem.OLCUM_TAMAMLA, "Ölçüm tamamlandı.");
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
            belgeService.upload(seciliTalep, EnumTabBelgeTuru.OLCU_FORMU, event.getFile(), belgeAciklama);
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

    /**
     * Oturumdaki kullanıcı adı (ölçümü yapan alanının salt okunur gösterimi).
     */
    public String getOlcumuYapan() {
        return Util.getSyKullanici() == null ? "" : Util.getSyKullanici().getKullaniciAdi();
    }

    private void refreshLists() {
        olcumler = seciliTalep == null ? Collections.emptyList() : service.findByTalep(seciliTalep);
        belgeler = seciliTalep == null ? Collections.emptyList() : belgeService.findByTalep(seciliTalep);
    }
}
