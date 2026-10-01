package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHemsireGozlemFormu;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 12:07
 */
@Named
@ViewScoped
@Slf4j
public class PkHemsireGozlemFormuController extends AbstractController<PkHemsireGozlemFormu> {

    @Serial
    private static final long serialVersionUID = 2665773225977389182L;

    public PkHemsireGozlemFormuController() {
        super(PkHemsireGozlemFormu.class);
    }

    @Override
    public PkHemsireGozlemFormu prepareCreate(ActionEvent event) {
        PkHemsireGozlemFormu newItem;
        try {
            newItem = PkHemsireGozlemFormu.class.getDeclaredConstructor().newInstance();
            newItem.setTarih(LocalDateTime.now());
            newItem.setPkHasta(PkHasta.builder().gnlKisi(new GnlKisi()).build());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkHasta(SelectEvent<PkHasta> event) {
        PkHasta pkHasta = event.getObject();
        this.getSelected().setPkHasta(pkHasta);
    }
}
