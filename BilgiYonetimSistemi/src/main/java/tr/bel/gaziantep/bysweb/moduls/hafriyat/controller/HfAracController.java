package tr.bel.gaziantep.bysweb.moduls.hafriyat.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfArac;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfFirma;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:20
 */
@Named
@ViewScoped
@Slf4j
public class HfAracController extends AbstractController<HfArac> {

    @Serial
    private static final long serialVersionUID = -8978042589421579583L;

    public HfAracController() {
        super(HfArac.class);
    }

    @Override
    public HfArac prepareCreate(ActionEvent event) {
        HfArac newItem;
        try {
            newItem = HfArac.class.getDeclaredConstructor().newInstance();
            newItem.setHfFirma(new HfFirma());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenHfFirma(SelectEvent<HfFirma> event) {
        HfFirma hfFirma = event.getObject();
        this.getSelected().setHfFirma(hfFirma);
    }

    public void hfAracSecKapat(HfArac hfArac) {
        PrimeFaces.current().dialog().closeDynamic(hfArac);
    }

    public void onRowDblSelect(SelectEvent<HfArac> event) {
        HfArac hfArac = event.getObject();
        hfAracSecKapat(hfArac);
    }
}
