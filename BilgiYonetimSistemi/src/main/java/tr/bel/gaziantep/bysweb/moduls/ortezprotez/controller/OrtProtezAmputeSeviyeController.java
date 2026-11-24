package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProtezAmputeSeviye;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 14:07
 */
@Named
@ViewScoped
@Slf4j
public class OrtProtezAmputeSeviyeController extends AbstractController<OrtProtezAmputeSeviye> {
    @Serial
    private static final long serialVersionUID = -3516127917039189291L;

    public OrtProtezAmputeSeviyeController() {
        super(OrtProtezAmputeSeviye.class);
    }
}
