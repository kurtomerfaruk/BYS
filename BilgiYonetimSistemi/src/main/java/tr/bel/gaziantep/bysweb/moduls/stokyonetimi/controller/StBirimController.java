package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StBirim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 09:25
 */
@Named
@ViewScoped
@Slf4j
public class StBirimController extends AbstractController<StBirim> {
    @Serial
    private static final long serialVersionUID = -7940031921551273520L;

    public StBirimController() {
        super(StBirim.class);
    }
}
