package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTedarikci;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 15:42
 */
@Named
@ViewScoped
@Slf4j
public class StTedarikciController extends AbstractController<StTedarikci> {

    @Serial
    private static final long serialVersionUID = -7641554488844907096L;

    public StTedarikciController() {
        super(StTedarikci.class);
    }
}
