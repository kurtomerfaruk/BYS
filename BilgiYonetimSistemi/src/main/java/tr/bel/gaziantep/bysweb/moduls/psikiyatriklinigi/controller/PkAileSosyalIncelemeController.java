package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAileSosyalInceleme;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 13:57
 */
@Named
@ViewScoped
@Slf4j
public class PkAileSosyalIncelemeController extends AbstractController<PkAileSosyalInceleme> {

    @Serial
    private static final long serialVersionUID = 6537899146957468727L;

    public PkAileSosyalIncelemeController() {
        super(PkAileSosyalInceleme.class);
    }

    @Override
    public PkAileSosyalInceleme prepareCreate(ActionEvent event) {
        PkAileSosyalInceleme newItem;
        try {
            newItem = PkAileSosyalInceleme.class.getDeclaredConstructor().newInstance();
            newItem.setTarih(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkAile(SelectEvent<PkAile> event) {
        PkAile pkAile = event.getObject();
        this.getSelected().setPkAile(pkAile);
    }
}
