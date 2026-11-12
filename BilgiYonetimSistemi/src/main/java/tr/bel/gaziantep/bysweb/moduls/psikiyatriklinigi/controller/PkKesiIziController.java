package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkKesiIzi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.29.0
 * @since 12.11.2026 11:35
 */
@Named
@ViewScoped
@Slf4j
public class PkKesiIziController extends AbstractController<PkKesiIzi> {

    @Serial
    private static final long serialVersionUID = 8520233567529768020L;

    public PkKesiIziController() {
        super(PkKesiIzi.class);
    }
}
