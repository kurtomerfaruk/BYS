package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlYabanciDil;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 08:26
 */
@Named
@ViewScoped
@Slf4j
public class GnlYabanciDilController extends AbstractController<GnlYabanciDil> {

    @Serial
    private static final long serialVersionUID = 4934263252325359235L;

    public GnlYabanciDilController() {
        super(GnlYabanciDil.class);
    }
}
