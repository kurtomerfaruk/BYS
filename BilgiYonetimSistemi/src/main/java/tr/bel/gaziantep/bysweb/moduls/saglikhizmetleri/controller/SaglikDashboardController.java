package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiKanTahlilSonucService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiKontrolService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShObeziteAnketService;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 13:47
 */
@Named
@ViewScoped
@Slf4j
public class SaglikDashboardController implements Serializable {

    @Serial
    private static final long serialVersionUID = -2557750057166674800L;

    @Inject
    private ShKisiService shKisiService;
    @Inject
    private ShKisiKontrolService shKisiKontrolService;
    @Inject
    private ShKisiKanTahlilSonucService shKisiKanTahlilSonucService;
    @Inject
    private ShObeziteAnketService shObeziteAnketService;

    @Getter
    @Setter
    private int totalPerson;
    @Getter
    @Setter
    private int totalPersonCheck;
    @Getter
    @Setter
    private int totalBloodTest;
    @Getter
    @Setter
    private int totalObesitySurvey;

    @PostConstruct
    public void init() {
        totalPerson = getTotalCount();
        totalPersonCheck = getPersonCheckCount();
        totalBloodTest = getBloodTestCount();
        totalObesitySurvey = getObesitySurveyCount();
    }

    private int getTotalCount() {
        return shKisiService.count();
    }

    private int getPersonCheckCount() {
        return shKisiKontrolService.count();
    }

    private int getBloodTestCount() {
        return shKisiKanTahlilSonucService.count();
    }
    private int getObesitySurveyCount() {
        return shObeziteAnketService.count();
    }
}
