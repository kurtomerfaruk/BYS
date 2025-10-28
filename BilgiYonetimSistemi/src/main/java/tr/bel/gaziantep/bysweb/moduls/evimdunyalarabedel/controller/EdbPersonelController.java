package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbPersonel;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 09:22
 */
@Named
@ViewScoped
@Slf4j
public class EdbPersonelController extends AbstractController<EdbPersonel> {
    @Serial
    private static final long serialVersionUID = 8088518168939123839L;

    public EdbPersonelController() {
        super(EdbPersonel.class);
    }
}
