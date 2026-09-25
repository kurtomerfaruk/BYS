package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkBireyselTedaviPlani;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 09:46
 */
@Named
@ViewScoped
@Slf4j
public class PkBireyselTedaviPlaniController extends AbstractController<PkBireyselTedaviPlani> {

    @Serial
    private static final long serialVersionUID = 6589970542763823859L;

    public PkBireyselTedaviPlaniController() {
        super(PkBireyselTedaviPlani.class);
    }

    @Override
    public PkBireyselTedaviPlani prepareCreate(ActionEvent event) {
        PkBireyselTedaviPlani newItem;
        try {
            newItem = PkBireyselTedaviPlani.class.getDeclaredConstructor().newInstance();
            newItem.setPkHasta(new PkHasta());
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

    public void secilenPkHasta(SelectEvent<PkHasta> event) {
        PkHasta pkHasta = event.getObject();
        this.getSelected().setPkHasta(pkHasta);
    }
}
