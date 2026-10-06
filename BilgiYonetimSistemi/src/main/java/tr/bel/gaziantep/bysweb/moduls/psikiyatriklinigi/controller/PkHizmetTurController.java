package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHizmetTur;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 5.10.2026 10:09
 */
@Named
@ViewScoped
@Slf4j
public class PkHizmetTurController extends AbstractController<PkHizmetTur> {

    @Serial
    private static final long serialVersionUID = 7646026631450413892L;

    public PkHizmetTurController() {
        super(PkHizmetTur.class);
    }
}
