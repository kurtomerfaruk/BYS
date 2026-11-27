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
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabBelge;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabRandevu;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTeslimat;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabBelgeService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabRandevuService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTeslimatService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Teslimat randevusu, imzalı form ve teslimat işlemleri.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Named
@ViewScoped
@Slf4j
public class TabTeslimatController extends AbstractController<TabTeslimat> {

    @Serial
    private static final long serialVersionUID = -9018273654029382751L;

    @Inject
    private TabTeslimatService service;
    @Inject
    private TabRandevuService randevuService;
    @Inject
    private TabBelgeService belgeService;
    @Inject
    private TabTalepWorkflowService workflowService;

    @Getter
    @Setter
    private TabTalep seciliTalep;
    @Getter
    @Setter
    private LocalDateTime randevuTarihi = LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0);
    @Getter
    @Setter
    private String aciklama;
    @Getter
    @Setter
    private String belgeAciklama;

    @Getter
    private List<TabTeslimat> teslimatlar = Collections.emptyList();
    @Getter
    private List<TabBelge> belgeler = Collections.emptyList();
    @Getter
    private List<TabRandevu> teslimatRandevulari = Collections.emptyList();

    public TabTeslimatController() {
        super(TabTeslimat.class);
    }

    public void onTalepSelected(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            this.seciliTalep = event.getObject();
            this.aciklama = null;
            refreshLists();
        }
    }

    public void scheduleAppointment() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Teslimat randevusu için talep seçilmelidir.");
            return;
        }
        if (randevuTarihi == null) {
            FacesUtil.addErrorMessage("Randevu tarihi zorunludur.");
            return;
        }
        try {
            workflowService.createAppointmentAndTransition(seciliTalep.getId(), EnumTabRandevuTuru.TESLIMAT,
                    randevuTarihi, aciklama);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            refreshLists();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    public void completeAppointment() {
        if (seciliTalep == null) {
            return;
        }
        TabRandevu randevu = teslimatRandevulari.stream()
                .filter(r -> r.getDurum() == EnumTabRandevuDurumu.PLANLANDI)
                .findFirst()
                .orElse(null);
        if (randevu == null) {
            FacesUtil.addErrorMessage("Gerçekleştirilecek planlı teslimat randevusu bulunamadı.");
            return;
        }
        try {
            randevuService.complete(randevu.getId());
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
            belgeService.upload(seciliTalep, EnumTabBelgeTuru.TESLIMAT_FORMU, event.getFile(), belgeAciklama);
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

    public void deliver() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Teslimat için talep seçilmelidir.");
            return;
        }
        try {
            workflowService.applyDelivery(seciliTalep.getId(), aciklama);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            refreshLists();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    private void refreshLists() {
        if (seciliTalep == null) {
            teslimatlar = Collections.emptyList();
            belgeler = Collections.emptyList();
            teslimatRandevulari = Collections.emptyList();
            return;
        }
        teslimatlar = service.findByTalep(seciliTalep);
        belgeler = belgeService.findByTalep(seciliTalep);
        teslimatRandevulari = randevuService.findByTalepAndTur(seciliTalep, EnumTabRandevuTuru.TESLIMAT);
    }
}
