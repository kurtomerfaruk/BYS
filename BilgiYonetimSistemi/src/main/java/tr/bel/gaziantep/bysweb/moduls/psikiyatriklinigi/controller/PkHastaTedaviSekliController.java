package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 10:44
 */
@Named
@ViewScoped
@Slf4j
public class PkHastaTedaviSekliController extends AbstractController<PkHastaTedaviSekli> {

    @Serial
    private static final long serialVersionUID = -147202773805226019L;

    public PkHastaTedaviSekliController() {
        super(PkHastaTedaviSekli.class);
    }

    @Override
    public PkHastaTedaviSekli prepareCreate(ActionEvent event) {
        PkHastaTedaviSekli newItem;
        try {
            newItem = PkHastaTedaviSekli.class.getDeclaredConstructor().newInstance();
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
