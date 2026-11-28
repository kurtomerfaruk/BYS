package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabRandevuTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabRandevu;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabRandevuService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:36
 */
@Named
@ViewScoped
@Slf4j
public class TabRandevuController extends AbstractController<TabRandevu> {

    @Serial
    private static final long serialVersionUID = 2196718127658519789L;

    @Inject
    private TabRandevuService service;
    @Inject
    private TabTalepWorkflowService workflowService;
    @Inject
    private TabTalepController tabTalepController;

    @Getter
    private ScheduleModel lazyEventModel;
    @Getter
    private List<TabRandevu> incomingEvents = Collections.emptyList();

    @Getter
    @Setter
    private String iptalNedeni;
    @Getter
    @Setter
    private String ertelemeNedeni;

    private ScheduleEvent<TabRandevu> event = new DefaultScheduleEvent<>();

    public TabRandevuController() {
        super(TabRandevu.class);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        lazyEventModel = new LazyScheduleModel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void loadEvents(LocalDateTime start, LocalDateTime end) {
                for (TabRandevu randevu : service.findByDateRange(start, end)) {
                    addEvent(DefaultScheduleEvent.<TabRandevu>builder()
                            .id(String.valueOf(randevu.getId()))
                            .title(buildEventTitle(randevu))
                            .startDate(randevu.getRandevuTarihi())
                            .endDate(randevu.getRandevuTarihi().plusHours(1))
                            .data(randevu)
                            .build());
                }
            }
        };
        refreshIncomingEvents();
    }

    private String buildEventTitle(TabRandevu randevu) {
        String tur = randevu.getRandevuTuru() == null ? "" : randevu.getRandevuTuru().getDisplayValue();
        String adSoyad = randevu.getTabKisi() != null && randevu.getTabKisi().getGnlKisi() != null
                ? randevu.getTabKisi().getGnlKisi().getAdSoyad() : "";
        return (adSoyad.isBlank() ? tur : adSoyad + " - " + tur);
    }

    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        try {
            LocalDateTime time = selectEvent.getObject();
            if (time.getHour() == 0) {
                time = time.withHour(9);
            }
            TabRandevu randevu = new TabRandevu();
            randevu.setRandevuTarihi(time);
            randevu.setDurum(EnumTabRandevuDurumu.PLANLANDI);
            this.setSelected(randevu);
            event = new DefaultScheduleEvent<>();
            event.setStartDate(time);
            event.setEndDate(time.plusHours(1));
            initializeEmbeddableKey();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @SuppressWarnings("unchecked")
    public void onEventSelect(SelectEvent<ScheduleEvent<TabRandevu>> selectEvent) {
        event = selectEvent.getObject();
        this.setSelected(event.getData());
        iptalNedeni = null;
        ertelemeNedeni = null;
    }

    /**
     * Talep ekranından "Randevu Ver" ile gelindiğinde formu hazırlar.
     */
    public void prepareCreateForTalep(TabTalep talep, EnumTabRandevuTuru tur) {
        TabRandevu randevu = new TabRandevu();
        randevu.setTabTalep(talep);
        randevu.setTabKisi(talep == null ? null : talep.getTabKisi());
        randevu.setRandevuTuru(tur == null ? EnumTabRandevuTuru.OLCU : tur);
        randevu.setRandevuTarihi(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0).withSecond(0).withNano(0));
        randevu.setDurum(EnumTabRandevuDurumu.PLANLANDI);
        this.setSelected(randevu);
        initializeEmbeddableKey();
    }


    /**
     * "Randevu Ver" butonuyla açılan talep seçim dialogundan dönen talep.
     */
    public void onTalepSelectedForCreate(SelectEvent<TabTalep> selectEvent) {
        if (selectEvent != null && selectEvent.getObject() != null) {
            prepareCreateForTalep(selectEvent.getObject(), EnumTabRandevuTuru.OLCU);
            PrimeFaces.current().executeScript("PF('TabRandevuCreateDialog').show()");
        }
    }

    /**
     * Kişi/talep seçim dialogundan dönen talep.
     */
    public void selectTalep(SelectEvent<TabTalep> selectEvent) {
        if (selectEvent != null && selectEvent.getObject() != null && this.getSelected() != null) {
            this.getSelected().setTabTalep(selectEvent.getObject());
            this.getSelected().setTabKisi(selectEvent.getObject().getTabKisi());
        }
    }

    @Override
    public void saveNew(ActionEvent actionEvent) {
        TabRandevu randevu = this.getSelected();
        if (randevu == null || randevu.getTabTalep() == null || randevu.getTabTalep().getId() == null) {
            FacesUtil.addErrorMessage("Randevu için talep seçilmelidir.");
            return;
        }
        if (randevu.getRandevuTuru() == null || randevu.getRandevuTarihi() == null) {
            FacesUtil.addErrorMessage("Randevu türü ve tarihi zorunludur.");
            return;
        }
        try {
            workflowService.createAppointmentAndTransition(
                    randevu.getTabTalep().getId(),
                    randevu.getRandevuTuru(),
                    randevu.getRandevuTarihi(),
                    randevu.getAciklama());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            refreshTalepDetail(randevu);
            if (!isValidationFailed()) {
                this.setItems(null);
                this.setLazyItems(null);
                this.setSelected(null);
            }
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
        refreshIncomingEvents();
    }

    @Override
    public void save(ActionEvent actionEvent) {
        TabRandevu secili = this.getSelected();
        if (secili == null || secili.getId() == null) {
            return;
        }
        // Durum değişiklikleri özel işlemlerle yapılır; genel kayıt sadece tarih/açıklama günceller.
        TabRandevu mevcut = service.find(secili.getId());
        secili.setDurum(mevcut == null ? secili.getDurum() : mevcut.getDurum());
        super.save(actionEvent);
        refreshIncomingEvents();
    }

    public void cancelSelected() {
        if (this.getSelected() == null || this.getSelected().getId() == null) {
            return;
        }
        try {
            service.cancel(this.getSelected().getId(), iptalNedeni);
            reloadSelected();
            refreshTalepDetail(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            PrimeFaces.current().executeScript("PF('TabRandevuEditDialog').hide()");
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
        refreshIncomingEvents();
    }

    public void postponeSelected() {
        if (this.getSelected() == null || this.getSelected().getId() == null) {
            return;
        }
        try {
            service.postpone(this.getSelected().getId(), ertelemeNedeni);
            reloadSelected();
            refreshTalepDetail(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
        refreshIncomingEvents();
    }

    /**
     * Detay ekranındaki randevu tablosundan doğrudan gerçekleşti işareti.
     */
    public void completeRandevu(TabRandevu randevu) {
        if (randevu == null) {
            return;
        }
        this.setSelected(randevu);
        completeSelected();
    }

    public void completeSelected() {
        if (this.getSelected() == null || this.getSelected().getId() == null) {
            return;
        }
        try {
            service.complete(this.getSelected().getId());
            reloadSelected();
            refreshTalepDetail(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
        refreshIncomingEvents();
    }

    private void reloadSelected() {
        if (this.getSelected() != null && this.getSelected().getId() != null) {
            this.setSelected(service.find(this.getSelected().getId()));
        }
    }

    /**
     * Ortez-Protez'deki refreshTalepDetail kalıbı: randevu işlemi talep detayını
     * değiştirdiğinde açık detay dialogunu anında tazeler; kapatıp açmaya gerek kalmaz.
     */
    private void refreshTalepDetail(TabRandevu randevu) {
        if (randevu == null || randevu.getTabTalep() == null || tabTalepController.getSelected() == null) {
            return;
        }
        if (java.util.Objects.equals(randevu.getTabTalep().getId(), tabTalepController.getSelected().getId())) {
            tabTalepController.readInfo();
            // Durum değiştiğinde detay dialogundaki "Sıradaki İşlem" kartı anında yenilenmeli.
            if (jakarta.faces.context.FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest()) {
                PrimeFaces.current().ajax().update(":TabTalepViewForm");
            }
        }
    }

    private void refreshIncomingEvents() {
        incomingEvents = service.findByDateRange(LocalDateTime.now(), DateUtil.lastOfMonth());
    }
}
