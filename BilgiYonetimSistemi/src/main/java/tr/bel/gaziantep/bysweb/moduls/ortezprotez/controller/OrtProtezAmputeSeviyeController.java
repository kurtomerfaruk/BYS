package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtProtezAmputeSeviye;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 6.11.2026 09:45
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
