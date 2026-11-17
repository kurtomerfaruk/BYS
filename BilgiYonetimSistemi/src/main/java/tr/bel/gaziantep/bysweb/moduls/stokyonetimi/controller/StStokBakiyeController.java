package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 12:46
 */
@Named
@ViewScoped
public class StStokBakiyeController extends AbstractController<StStokBakiye> {
    @Serial
    private static final long serialVersionUID = 3038665639557487630L;

    public StStokBakiyeController() {
        super(StStokBakiye.class);
    }

}
