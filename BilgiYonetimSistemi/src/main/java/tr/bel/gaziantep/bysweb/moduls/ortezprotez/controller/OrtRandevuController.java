package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.*;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtRandevuService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepWorkflowService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 08:33
 */
@Named
@ViewScoped
@Slf4j
public class OrtRandevuController extends AbstractController<OrtRandevu> {

    @Serial
    private static final long serialVersionUID = -3691222077414926257L;

    @Inject
    private OrtRandevuService service;
    @Inject
    private OrtTalepWorkflowService workflowService;

    @Getter
    private ScheduleModel lazyEventModel;
    @Getter
    private ScheduleModel eventModel;
    @Getter
    private List<OrtRandevu> incomingEvents = Collections.emptyList();

    private ScheduleEvent<OrtRandevu> event = new DefaultScheduleEvent<>();

    public OrtRandevuController() {
        super(OrtRandevu.class);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        eventModel = new DefaultScheduleModel();
        lazyEventModel = new LazyScheduleModel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void loadEvents(LocalDateTime start, LocalDateTime end) {
                for (OrtRandevu randevu : service.findByDateRange(start, end)) {
                    String title = buildEventTitle(randevu);
                    addEvent(DefaultScheduleEvent.<OrtRandevu>builder()
                            .id(String.valueOf(randevu.getId()))
                            .title(title)
                            .startDate(randevu.getRandevuTarihi())
                            .endDate(randevu.getRandevuTarihi().plusHours(1))
                            .data(randevu)
                            .build());
                }
            }
        };
        incomingEvents = service.findByDateRange(LocalDateTime.now(), DateUtil.lastOfMonth());
    }

    private String buildEventTitle(OrtRandevu randevu) {
        String tip = randevu.getRandevuTuru() == null ? "" : randevu.getRandevuTuru().getDisplayValue();
        String adSoyad = randevu.getOrtKisi() != null && randevu.getOrtKisi().getGnlKisi() != null
                ? randevu.getOrtKisi().getGnlKisi().getAdSoyad() : "";
        return (adSoyad.isBlank() ? tip : adSoyad + " - " + tip);
    }

    /**
     * Takvimde boş bir güne tıklandığında yeni randevu dialogunu açar.
     */
    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        try {
            LocalDateTime time = selectEvent.getObject();
            if (time.getHour() == 0) {
                time = time.withHour(9);
            }
            OrtRandevu randevu = OrtRandevu.class.getDeclaredConstructor().newInstance();
            randevu.setRandevuTarihi(time);
            randevu.setDurum(EnumOrtRandevuDurumu.PLANLANDI);
            this.setSelected(randevu);
            this.event = new DefaultScheduleEvent<>();
            event.setStartDate(time);
            event.setEndDate(time.plusHours(1));
            initializeEmbeddableKey();
            PrimeFaces.current().executeScript("PF('OrtRandevuCreateDialog').show()");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Takvimdeki bir randevuya tıklandığında düzenleme dialogunu açar.
     */
    @SuppressWarnings("unchecked")
    public void onEventSelect(SelectEvent<ScheduleEvent<OrtRandevu>> selectEvent) {
        event = selectEvent.getObject();
        this.setSelected(event.getData());
    }

    public void prepareCreateForTalep(OrtTalep talep) {
        try {
            OrtRandevu randevu = OrtRandevu.class.getDeclaredConstructor().newInstance();
            randevu.setOrtTalep(talep);
            randevu.setOrtKisi(talep == null ? null : talep.getOrtKisi());
            randevu.setRandevuTarihi(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0));
            randevu.setDurum(EnumOrtRandevuDurumu.PLANLANDI);
            this.setSelected(randevu);
            initializeEmbeddableKey();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void saveNew(ActionEvent event) {
        super.saveNew(event);
        refreshIncomingEvents();
    }

    @Override
    public void save(ActionEvent event) {
        OrtRandevu secili = this.getSelected();
        EnumOrtRandevuDurumu oncekiDurum = null;
        if (secili != null && secili.getId() != null) {
            OrtRandevu mevcut = service.find(secili.getId());
            oncekiDurum = mevcut == null ? null : mevcut.getDurum();
        }
        super.save(event);
        if (secili != null && secili.getId() != null && secili.getDurum() != oncekiDurum
                && (secili.getDurum() == EnumOrtRandevuDurumu.GELDI
                || secili.getDurum() == EnumOrtRandevuDurumu.TAMAMLANDI)) {
            applyTransitionAfterSave(secili, oncekiDurum);
        }
        refreshIncomingEvents();
    }

    /**
     * Düzenleme dialogundan durum değiştirildiğinde talep geçişini uygular.
     */
    private void applyTransitionAfterSave(OrtRandevu randevu, EnumOrtRandevuDurumu oncekiDurum) {
        try {
            workflowService.applyAppointmentTransition(randevu.getId(), randevu.getDurum());
        } catch (RuntimeException ex) {
            // Talep geçişi başarısızsa randevu durumunu geri al.
            randevu.setDurum(oncekiDurum);
            service.edit(randevu);
            service.flush();
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    private void refreshIncomingEvents() {
        incomingEvents = service.findByDateRange(LocalDateTime.now(), DateUtil.lastOfMonth());
    }

    public List<OrtRandevu> getTalepRandevulari(OrtTalep talep) {
        if (talep == null) {
            return Collections.emptyList();
        }
        return service.findByTalep(talep);
    }

    public void markAttended(OrtRandevu randevu) {
        updateStatus(randevu, EnumOrtRandevuDurumu.GELDI);
    }

    public void markCompleted(OrtRandevu randevu) {
        updateStatus(randevu, EnumOrtRandevuDurumu.TAMAMLANDI);
    }

    public void markNotAttended(OrtRandevu randevu) {
        updateStatus(randevu, EnumOrtRandevuDurumu.GELMEDI);
    }

    private void updateStatus(OrtRandevu randevu, EnumOrtRandevuDurumu durum) {
        if (randevu == null || randevu.getId() == null) {
            FacesUtil.addErrorMessage("Randevu kaydı bulunamadı.");
            return;
        }
        try {
            EnumOrtRandevuDurumu oncekiDurum = randevu.getDurum();
            randevu.setDurum(durum);
            service.edit(randevu);
            service.flush();

            // Randevu tamamlandığında talebin durumunu da merkezi workflow üzerinden ilerlet.
            try {
                workflowService.applyAppointmentTransition(randevu.getId(), durum);
            } catch (RuntimeException ex) {
                // Tutarsızlığı önlemek için randevu durumunu geri al.
                randevu.setDurum(oncekiDurum);
                service.edit(randevu);
                service.flush();
                throw ex;
            }

            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            refreshIncomingEvents();
        } catch (RuntimeException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void selectTalep(SelectEvent<OrtTalep> event) {
        if (this.getSelected() != null) {
            this.getSelected().setOrtTalep(event.getObject());
            this.getSelected().setOrtKisi(event.getObject() == null ? null : event.getObject().getOrtKisi());
        }
    }
}


