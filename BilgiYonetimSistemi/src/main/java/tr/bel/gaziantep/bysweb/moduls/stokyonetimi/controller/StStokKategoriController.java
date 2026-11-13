package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokKategori;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 11:27
 */
@Named
@ViewScoped
@Slf4j
public class StStokKategoriController extends AbstractController<StStokKategori> {

    @Serial
    private static final long serialVersionUID = -5925587332064642582L;

    public StStokKategoriController() {
        super(StStokKategori.class);
    }

    @Override
    public StStokKategori prepareCreate(ActionEvent event) {
        StStokKategori newItem;
        try {
            newItem = StStokKategori.class.getDeclaredConstructor().newInstance();
            newItem.setUstKategori(new StStokKategori());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }
}
