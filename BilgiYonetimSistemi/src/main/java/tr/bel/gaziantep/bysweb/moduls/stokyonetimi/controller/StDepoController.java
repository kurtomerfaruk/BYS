package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 09:34
 */
@Named
@ViewScoped
@Slf4j
public class StDepoController extends AbstractController<StDepo> {


    @Serial
    private static final long serialVersionUID = 6269179155296421567L;

    public StDepoController() {
        super(StDepo.class);
    }
}