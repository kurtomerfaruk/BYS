package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 13:36
 */
@Named
@ViewScoped
@Slf4j
public class GenelDashboardController implements Serializable {
    @Serial
    private static final long serialVersionUID = -7362827941504207985L;

    @Inject
    private GnlKisiService gnlKisiService;

    @Getter
    @Setter
    private int totalPerson;

    @PostConstruct
    public void init() {
        totalPerson = getTotalCount();
    }

    private int getTotalCount() {
        return gnlKisiService.count();
    }
}
