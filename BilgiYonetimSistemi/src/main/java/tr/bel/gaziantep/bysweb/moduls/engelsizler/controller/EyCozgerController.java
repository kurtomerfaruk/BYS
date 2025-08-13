package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyCozger;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:37
 */
@Named
@ViewScoped
@Slf4j
public class EyCozgerController extends AbstractController<EyCozger> {

    @Serial
    private static final long serialVersionUID = 4745209483393209232L;

    public EyCozgerController() {
        super(EyCozger.class);
    }
}
