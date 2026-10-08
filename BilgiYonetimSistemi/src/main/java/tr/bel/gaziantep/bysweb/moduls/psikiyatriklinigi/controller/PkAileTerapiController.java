package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAileTerapi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 13:29
 */
@Named
@ViewScoped
@Slf4j
public class PkAileTerapiController extends AbstractController<PkAileTerapi> {

    @Serial
    private static final long serialVersionUID = 9180811450693583864L;

    public PkAileTerapiController() {
        super(PkAileTerapi.class);
    }

    @Override
    public PkAileTerapi prepareCreate(ActionEvent event) {
        PkAileTerapi newItem;
        try {
            newItem = PkAileTerapi.class.getDeclaredConstructor().newInstance();
            newItem.setPkAile(PkAile.builder().gnlKisi(new GnlKisi()).build());
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
