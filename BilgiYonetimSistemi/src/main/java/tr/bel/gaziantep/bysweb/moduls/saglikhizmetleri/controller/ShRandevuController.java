package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.*;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShRandevu;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShRandevuService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 21.05.2026 11:26
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class ShRandevuController extends AbstractController<ShRandevu> {

    @Serial
    private static final long serialVersionUID = 7901031930210993401L;

    @Inject
    private ShRandevuService service;
    @Inject
    private ShKisiService shKisiService;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kpsController;

    private ScheduleModel eventModel;
    private String serverTimeZone = ZoneId.systemDefault().toString();
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    private ScheduleModel lazyEventModel;
    private List<ShRandevu> incomingEvents;

    public ShRandevuController() {
        super(ShRandevu.class);
    }

    @Override
    @PostConstruct
    public void init(){
        eventModel = new DefaultScheduleModel();

        lazyEventModel = new LazyScheduleModel() {
            @Serial
            private static final long serialVersionUID = -2558451412134414912L;

            @Override
            public void loadEvents(LocalDateTime start, LocalDateTime end) {
                List<ShRandevu> randevus = service.findByStartDateByEndDate(start, end);
                for (ShRandevu randevu : randevus) {
                    String title = randevu.getKonu();
                    if (randevu.getShKisi() != null && randevu.getShKisi().getGnlKisi() != null) {
                        title = randevu.getShKisi().getGnlKisi().getAdSoyad() + " - " + title;
                    }
                    addEvent(DefaultScheduleEvent.builder()
                            .title(title)
                            .startDate(randevu.getRandevuTarihi())
                            .endDate(randevu.getRandevuTarihi().plusHours(1))
                            .data(randevu)
                            .build());
                }
            }
        };

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = DateUtil.lastOfMonth();
        incomingEvents=service.findByStartDateByEndDate(start,end);
    }

    public void onDateSelect(SelectEvent<LocalDateTime> selectEvent) {
        try {
            LocalDateTime time = selectEvent.getObject();
            if (time.getHour() == 0) {
                time = time.withHour(8);
            }
            event.setStartDate(time);
            event.setEndDate(time.plusHours(1));
            GnlPersonel personel = this.getSyKullanici().getGnlPersonel();
            ShRandevu shRandevu = ShRandevu.builder()
                    .randevuTarihi(time)
                    .shKisi(ShKisi.builder().gnlKisi(new GnlKisi()).build())
                    .gnlPersonel(personel)
                    .build();
            this.setSelected(shRandevu);
            PrimeFaces.current().executeScript("PF('ShRandevuCreateDialog').show()");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void onEventSelect(SelectEvent<ScheduleEvent<ShRandevu>> selectEvent) {
        event = selectEvent.getObject();
        this.setSelected((ShRandevu) event.getData());
    }

    public void update(ActionEvent actionEvent){
        try {
            if (event.isAllDay() && (event.getStartDate().toLocalDate().equals(event.getEndDate().toLocalDate()))) {
                event.setEndDate(event.getEndDate().plusDays(1));
            }

            if (event.getId() == null) {
                eventModel.addEvent(event);
                this.saveNew(actionEvent);
            } else {
                eventModel.updateEvent(event);
                this.save(actionEvent);
            }
            event = new DefaultScheduleEvent<>();
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void secilenShKisi(SelectEvent<ShKisi> event) {
        ShKisi shKisi = event.getObject();
        if (this.getSelected() != null) {
            this.getSelected().setShKisi(shKisi);
        }
    }

    public void yeniShKisiKaydet(ActionEvent actionEvent) {
        try {
            if (this.getSelected() != null && this.getSelected().getShKisi() != null) {
                ShKisi shKisi = this.getSelected().getShKisi();
                String tcKimlikNo = shKisi.getGnlKisi().getTcKimlikNo();
                if (tcKimlikNo != null) {
                    GnlKisi mevcutKisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                    if (mevcutKisi != null) {
                        boolean exist = shKisiService.existByGnlKisi(mevcutKisi);
                        if (exist) {
                            FacesUtil.warningMessage("mukerrerKisiKayit");
                            return;
                        }
                        shKisi.setGnlKisi(mevcutKisi);
                    }
                }
                shKisi.setGelisTarihi(LocalDateTime.now());
                shKisiService.create(shKisi);
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                PrimeFaces.current().executeScript("PF('ShRandevuShKisiCreateDialog').hide()");
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

//    public void prepareCreateShKisi(ActionEvent actionEvent) {
//        try {
//            GnlKisi kisi = GnlKisi.builder()
//                    .kayitTarihi(java.time.LocalDate.now())
//                    .eklemeYeri(EnumModul.SAGLIK_HIZMETLERI)
//                    .build();
//            ShKisi shKisi = ShKisi.builder()
//                    .gnlKisi(kisi)
//                    .gelisTarihi(LocalDateTime.now())
//                    .build();
//            if (this.getSelected() != null) {
//                this.getSelected().setShKisi(shKisi);
//            }
//        } catch (Exception ex) {
//            log.error(null, ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//        }
//    }

    public void getTcKimlik() {
        if (this.getSelected() != null && this.getSelected().getShKisi() != null) {
            try {
                String tcKimlikNo = this.getSelected().getShKisi().getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) {
                    kisi = this.getSelected().getShKisi().getGnlKisi();
                }
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.SAGLIK_HIZMETLERI);
                this.getSelected().getShKisi().setGnlKisi(kisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}
