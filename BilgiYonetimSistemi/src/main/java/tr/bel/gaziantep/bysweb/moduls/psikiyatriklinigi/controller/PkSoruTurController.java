package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkSoruTur;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 2.10.2026 09:10
 */
@Named
@ViewScoped
@Slf4j
public class PkSoruTurController extends AbstractController<PkSoruTur> {

    @Serial
    private static final long serialVersionUID = -8372212706979429952L;

    public PkSoruTurController() {
        super(PkSoruTur.class);
    }
}
