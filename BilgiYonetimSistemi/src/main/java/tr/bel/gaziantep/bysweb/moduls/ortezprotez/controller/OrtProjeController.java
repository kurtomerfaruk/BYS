package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProje;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:59
 */
@Named
@ViewScoped
@Slf4j
public class OrtProjeController extends AbstractController<OrtProje> {
    @Serial
    private static final long serialVersionUID = -1814935313900566297L;

    public OrtProjeController() {
        super(OrtProje.class);
    }
}
