package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlHastane;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 3.11.2026 08:45
 */
@Named
@ViewScoped
@Slf4j
public class GnlHastaneController extends AbstractController<GnlHastane> {

    @Serial
    private static final long serialVersionUID = 2003888717031163636L;

    public GnlHastaneController() {
        super(GnlHastane.class);
    }
}
