package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyArac;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 10:25
 */
@Named
@ViewScoped
@Slf4j
public class EyAracController extends AbstractController<EyArac> {

    @Serial
    private static final long serialVersionUID = -5973853044553214487L;

    public EyAracController() {
        super(EyArac.class);
    }
}
