package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkBireyselGorusme;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 13:48
 */
@Named
@ViewScoped
@Slf4j
public class PkBireyselGorusmeController extends AbstractController<PkBireyselGorusme> {

    @Serial
    private static final long serialVersionUID = 2570749221416333870L;

    public PkBireyselGorusmeController() {
        super(PkBireyselGorusme.class);
    }

    @Override
    public PkBireyselGorusme prepareCreate(ActionEvent event) {
        PkBireyselGorusme newItem;
        try {
            newItem = PkBireyselGorusme.class.getDeclaredConstructor().newInstance();
            newItem.setPkAile(new PkAile());
            newItem.setTarih(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkAile(SelectEvent<PkAile> event) {
        PkAile pkAile = event.getObject();
        this.getSelected().setPkAile(pkAile);
    }
}
