package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtSutKodu;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 16:15
 */
@Named
@ViewScoped
@Slf4j
public class OrtSutKoduController extends AbstractController<OrtSutKodu> {

    @Serial
    private static final long serialVersionUID = -5801596735758057085L;

    public OrtSutKoduController() {
        super(OrtSutKodu.class);
    }
}
