package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracCihazTeslimiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracTamirService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepService;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 10:40
 */
@Named
@ViewScoped
@Slf4j
public class EngelsizDashboardController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1541720949136400365L;

    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private EyAracTamirService eyAracTamirService;
    @Inject
    private EyAracCihazTeslimiService eyAracCihazTeslimiService;
    @Inject
    private EyTalepService eyTalepService;

    @Getter
    @Setter
    private int totalPerson;
    @Getter
    @Setter
    private int deadPerson;
    @Getter
    @Setter
    private int activePerson;
    @Getter
    @Setter
    private int repairedVehicle;
    @Getter
    @Setter
    private int deliveredVehicle;
    @Getter
    @Setter
    private int request;

    @PostConstruct
    public void init() {
        totalPerson = getTotalCount();
        deadPerson = getTotalDeadCount();
        activePerson = getTotalActiveCount();
        repairedVehicle = getTotalRepairedVehicle();
        deliveredVehicle = getTotalDeliveredVehicle();
        request =getTotalRequest();
    }

    private int getTotalCount() {
        return eyKisiService.count();
    }

    private int getTotalDeadCount() {
        return eyKisiService.getTotalDeadCount();
    }

    private int getTotalActiveCount() {
        return eyKisiService.getTotalActiveCount();
    }

    private int getTotalRepairedVehicle() {
        return eyAracTamirService.count();
    }

    private int getTotalDeliveredVehicle() {
        return eyAracCihazTeslimiService.count();
    }

    private int getTotalRequest() {
        return eyTalepService.count();
    }


}
