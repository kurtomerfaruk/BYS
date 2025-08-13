package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlBanka;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 10:09
 */
@Named
@ViewScoped
@Slf4j
public class GnlBankaController extends AbstractController<GnlBanka> {

    @Serial
    private static final long serialVersionUID = 2149560949542342007L;

    @Inject
    private FilterOptionService filterOptionService;

    public GnlBankaController() {
        super(GnlBanka.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case IL -> {
                return filterOptionService.getGnlIls();
            }
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public GnlBanka prepareCreate(ActionEvent event) {
        GnlBanka newItem;
        try {
            newItem = GnlBanka.class.getDeclaredConstructor().newInstance();
            newItem.setGnlIl(new GnlIl());
            newItem.setGnlIlce(new GnlIlce());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }
}
