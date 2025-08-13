package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyBirim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 10:10
 */
@Named
@ViewScoped
@Slf4j
public class AyBirimController extends AbstractController<AyBirim> {
    @Serial
    private static final long serialVersionUID = 7340133878553697078L;

    public AyBirimController() {
        super(AyBirim.class);
    }
}
