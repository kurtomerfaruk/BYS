package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlHastalik;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 6.04.2026 14:33
 */
@Named
@ViewScoped
@Slf4j
public class GnlHastalikController extends AbstractController<GnlHastalik> {
    @Serial
    private static final long serialVersionUID = -7099995854891904819L;

    public GnlHastalikController() {
        super(GnlHastalik.class);
    }
}
