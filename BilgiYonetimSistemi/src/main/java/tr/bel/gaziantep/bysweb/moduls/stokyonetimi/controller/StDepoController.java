package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:56
 */
@Named
@ViewScoped
@Slf4j
public class StDepoController extends AbstractController<StDepo> {

    @Serial
    private static final long serialVersionUID = 6269179155296421567L;

    @Inject
    private FilterOptionService filterOptionService;

    public StDepoController() {
        super(StDepo.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STDEPO_TIPI -> {
                return filterOptionService.getStDepoTipis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
