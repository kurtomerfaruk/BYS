package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeAnket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 15:20
 */
@Named
@ViewScoped
@Slf4j
public class MeAnketController extends AbstractController<MeAnket> {

    @Serial
    private static final long serialVersionUID = -4720427074872508309L;

    public MeAnketController() {
        super(MeAnket.class);
    }
}
