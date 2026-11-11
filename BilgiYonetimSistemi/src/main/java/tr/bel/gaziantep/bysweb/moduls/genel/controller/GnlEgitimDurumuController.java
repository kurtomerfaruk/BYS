package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlEgitimDurumu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.29.0
 * @since 11.11.2026 15:11
 */
@Named
@ViewScoped
@Slf4j
public class GnlEgitimDurumuController extends AbstractController<GnlEgitimDurumu> {

    @Serial
    private static final long serialVersionUID = -7169525119350368642L;

    public GnlEgitimDurumuController() {
        super(GnlEgitimDurumu.class);
    }
}
