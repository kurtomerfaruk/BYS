package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAnketSoruTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnket;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketSoru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlAnketSoruSecenek;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlAnketService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.09.2025 11:27
 */
@Named
@ViewScoped
@Slf4j
public class GnlAnketController extends AbstractController<GnlAnket> {

    @Serial
    private static final long serialVersionUID = 503172173997319467L;

    @Inject
    private GnlAnketService gnlAnketService;


    public GnlAnketController() {
        super(GnlAnket.class);
    }


    @Override
    public GnlAnket prepareCreate(ActionEvent event) {
        GnlAnket newItem;
        try {
            newItem = GnlAnket.class.getDeclaredConstructor().newInstance();
            newItem.setGnlAnketSoruList(new ArrayList<>());
            newItem.setToken(UUID.randomUUID().toString().substring(0, 8));
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void addQuestion() {
        if (this.getSelected() == null) return;
        GnlAnketSoru soru = new GnlAnketSoru();
        soru.setGnlAnket(this.getSelected());
        soru.setGnlAnketSoruSecenekList(new ArrayList<>());
        soru.setSoruTuru(EnumGnlAnketSoruTuru.TEXT);
        this.getSelected().getGnlAnketSoruList().add(soru);
    }

    public void addOption(GnlAnketSoru gnlAnketSoru) {
        GnlAnketSoruSecenek gnlAnketSoruSecenek = new GnlAnketSoruSecenek();
        gnlAnketSoruSecenek.setGnlAnketSoru(gnlAnketSoru);
        gnlAnketSoru.getGnlAnketSoruSecenekList().add(gnlAnketSoruSecenek);
    }

    public void removeQuestion(GnlAnketSoru gnlAnketSoru) {
        this.getSelected().getGnlAnketSoruList().remove(gnlAnketSoru);
    }

    public void removeOption(GnlAnketSoru q, GnlAnketSoruSecenek o) {
        q.getGnlAnketSoruSecenekList().remove(o);
    }

}
