package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AySanatsalBeceri;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:02
 */
@Named
@ViewScoped
@Slf4j
public class AySanatsalBeceriController extends AbstractController<AySanatsalBeceri> {

    @Serial
    private static final long serialVersionUID = 3147606929301569376L;

    public AySanatsalBeceriController() {
        super(AySanatsalBeceri.class);
    }
}
