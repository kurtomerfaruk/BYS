package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

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
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtRandevuService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:09
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class OrtRandevuController extends AbstractController<OrtRandevu> {

    @Serial
    private static final long serialVersionUID = -3691222077414926257L;

    @Inject
    private OrtRandevuService service;

    private ScheduleModel eventModel;
    private String serverTimeZone = ZoneId.systemDefault().toString();
    private ScheduleEvent<?> event = new DefaultScheduleEvent<>();
    private ScheduleModel lazyEventModel;
    private List<OrtRandevu> incomingEvents;

    public OrtRandevuController() {
        super(OrtRandevu.class);
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
                List<OrtRandevu> randevus = service.findByStartDateByEndDate(start, end);
                for (OrtRandevu etEtkinlik : randevus) {
                    addEvent(DefaultScheduleEvent.builder()
                            .title(etEtkinlik.getKonu())
                            .startDate(etEtkinlik.getRandevuTarihi())
                            .endDate(etEtkinlik.getRandevuTarihi().plusHours(1))
                            .data(etEtkinlik)
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
            OrtRandevu ortRandevu = OrtRandevu.builder()
                    .randevuTarihi(time)
                    .build();
            this.setSelected(ortRandevu);
            PrimeFaces.current().executeScript("PF('OrtRandevuCreateDialog').show()");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void onEventSelect(SelectEvent<ScheduleEvent<OrtRandevu>> selectEvent) {
        event = selectEvent.getObject();
        this.setSelected((OrtRandevu) event.getData());
    }

    public  void update(ActionEvent actionEvent){
       try {
           if (event.isAllDay() && (event.getStartDate().toLocalDate().equals(event.getEndDate().toLocalDate()))) {
               event.setEndDate(event.getEndDate().plusDays(1));

           }

           if (event.getId() == null) {
               eventModel.addEvent(event);

           } else {
               eventModel.updateEvent(event);
           }
           this.save(actionEvent);
           event = new DefaultScheduleEvent<>();
       }catch (Exception ex){
           log.error(null,ex);
           FacesUtil.errorMessage(Constants.HATA_OLUSTU);
       }
    }

}
