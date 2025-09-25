package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.09.2025 08:40
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

    @Override
    public OrtPersonel prepareCreate(ActionEvent event) {
        OrtPersonel newItem;
        try {
            newItem = OrtPersonel.class.getDeclaredConstructor().newInstance();
            newItem.setGnlPersonel(new GnlPersonel());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }
}
