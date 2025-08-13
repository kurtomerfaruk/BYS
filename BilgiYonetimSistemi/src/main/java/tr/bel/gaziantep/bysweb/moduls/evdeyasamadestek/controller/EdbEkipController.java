package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbEkip;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:55
 */
@Named
@ViewScoped
@Slf4j
public class EdbEkipController extends AbstractController<EdbEkip> {

    @Serial
    private static final long serialVersionUID = 7700963987774822980L;

    public EdbEkipController() {
        super(EdbEkip.class);
    }
}
