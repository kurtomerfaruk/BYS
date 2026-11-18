package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTedarikci;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 14:14
 */
@Named
@ViewScoped
@Slf4j
public class StTedarikciController extends AbstractController<StTedarikci> {

    @Serial
    private static final long serialVersionUID = -7641554488844907096L;

    @Inject
    private FilterOptionService filterOptionService;

    public StTedarikciController() {
        super(StTedarikci.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STTEDARIKCI_TUR -> {
                return filterOptionService.getStTedarikciTurs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
