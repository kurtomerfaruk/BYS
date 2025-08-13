package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyAktivite;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:01
 */
@Named
@ViewScoped
@Slf4j
public class AyAktiviteController extends AbstractController<AyAktivite> {
    @Serial
    private static final long serialVersionUID = -1141197419148749507L;

    public AyAktiviteController() {
        super(AyAktivite.class);
    }
}
