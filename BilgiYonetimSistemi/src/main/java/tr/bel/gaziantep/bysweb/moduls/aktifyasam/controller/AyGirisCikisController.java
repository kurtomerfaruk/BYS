package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyGirisCikisService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.07.2025 09:03
 */
@Named
@ViewScoped
@Slf4j
public class AyGirisCikisController extends AbstractController<AyGirisCikis> {
    @Serial
    private static final long serialVersionUID = -5969226975342744990L;

    @Inject
    private AyGirisCikisService girisCikisService;

    @Getter
    @Setter
    private List<AyKisi> ayKisiList;

    public AyGirisCikisController() {
        super(AyGirisCikis.class);
    }

    @Override
    public AyGirisCikis prepareCreate(ActionEvent event) {
        AyGirisCikis newItem;
        try {
            newItem = AyGirisCikis.class.getDeclaredConstructor().newInstance();
            newItem.setGirisTarihi(LocalDateTime.now());
            newItem.setAyKisi(new AyKisi());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenAyKisi(SelectEvent<AyKisi> event) {
        AyKisi ayKisi = event.getObject();
        this.getSelected().setAyKisi(ayKisi);
    }

    public void createExitList() {
        ayKisiList = girisCikisService.getEntrants();
        this.getSelected().setCikisTarihi(LocalDateTime.now());
    }
}
