package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 13:58
 */
@Named
@ViewScoped
@Slf4j
public class OrtStokController extends AbstractController<OrtStok> {

    @Serial
    private static final long serialVersionUID = -3373843224409827700L;

    public OrtStokController() {
        super(OrtStok.class);
    }
}
