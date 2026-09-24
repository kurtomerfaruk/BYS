package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkMadde;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 13:28
 */
@Named
@ViewScoped
@Slf4j
public class PkMaddeController extends AbstractController<PkMadde> {

    @Serial
    private static final long serialVersionUID = 6743802053022852461L;

    public PkMaddeController() {
        super(PkMadde.class);
    }
}
