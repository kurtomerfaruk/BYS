package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelinOlusumu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 3.11.2026 11:14
 */
@Named
@ViewScoped
@Slf4j
public class EyEngelinOlusumuController extends AbstractController<EyEngelinOlusumu> {

    @Serial
    private static final long serialVersionUID = -8884400006062915185L;

    public EyEngelinOlusumuController() {
        super(EyEngelinOlusumu.class);
    }
}
