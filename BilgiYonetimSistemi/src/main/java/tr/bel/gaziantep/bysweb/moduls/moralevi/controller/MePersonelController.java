package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MePersonel;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 15:21
 */
@Named
@ViewScoped
@Slf4j
public class MePersonelController extends AbstractController<MePersonel> {

    @Serial
    private static final long serialVersionUID = 5881645009786124670L;

    public MePersonelController() {
        super(MePersonel.class);
    }

    @Override
    public MePersonel prepareCreate(ActionEvent event) {
        MePersonel newItem;
        try {
            newItem = MePersonel.class.getDeclaredConstructor().newInstance();
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
