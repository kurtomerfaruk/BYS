package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlBirim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 08:23
 */
@Named
@ViewScoped
@Slf4j
public class GnlBirimController extends AbstractController<GnlBirim> {
    @Serial
    private static final long serialVersionUID = 8934954611261822593L;

    public GnlBirimController() {
        super(GnlBirim.class);
    }
}
