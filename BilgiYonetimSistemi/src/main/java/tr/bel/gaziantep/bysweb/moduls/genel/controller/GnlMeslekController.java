package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMeslek;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 2.11.2026 16:23
 */
@Named
@ViewScoped
@Slf4j
public class GnlMeslekController extends AbstractController<GnlMeslek> {

    @Serial
    private static final long serialVersionUID = -8036299429403601325L;

    public GnlMeslekController() {
        super(GnlMeslek.class);
    }
}
