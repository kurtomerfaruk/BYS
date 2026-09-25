package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkTaburcuNedeni;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 11:08
 */
@Named
@ViewScoped
@Slf4j
public class PkTaburcuNedeniController extends AbstractController<PkTaburcuNedeni> {

    @Serial
    private static final long serialVersionUID = -8613493984976409616L;

    public PkTaburcuNedeniController() {
        super(PkTaburcuNedeni.class);
    }
}
