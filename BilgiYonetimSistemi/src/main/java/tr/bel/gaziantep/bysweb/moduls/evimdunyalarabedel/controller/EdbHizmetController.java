package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmet;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 09:01
 */
@Named
@ViewScoped
@Slf4j
public class EdbHizmetController extends AbstractController<EdbHizmet> {

    @Serial
    private static final long serialVersionUID = -9105843979853102610L;

    @Inject
    private FilterOptionService filterOptionService;

    public EdbHizmetController() {
        super(EdbHizmet.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EDBHIZMET_TURU -> {
                return filterOptionService.getEdbHizmetTurus();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }
}
