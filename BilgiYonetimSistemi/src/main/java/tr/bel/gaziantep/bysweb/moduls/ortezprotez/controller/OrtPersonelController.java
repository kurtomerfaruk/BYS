package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;

import java.io.Serial;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:00
 */
@Named
@ViewScoped
@Slf4j
public class OrtPersonelController extends AbstractController<OrtPersonel> {
    @Serial
    private static final long serialVersionUID = -7090806099510249798L;

    public OrtPersonelController() {
        super(OrtPersonel.class);
    }

    public void ortPersonelSecKapat(OrtPersonel ortPersonel) {
        PrimeFaces.current().dialog().closeDynamic(ortPersonel);
    }

    public void onRowDblSelect(SelectEvent<OrtPersonel> event) {
        ortPersonelSecKapat(event.getObject());
    }
}
