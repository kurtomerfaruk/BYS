package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 10:54
 */
@Named
@ViewScoped
@Slf4j
public class StDepoLokasyonController extends AbstractController<StDepoLokasyon> {

    @Serial
    private static final long serialVersionUID = 3088507083220304712L;

    @Inject
    private FilterOptionService filterOptionService;

    public StDepoLokasyonController() {
        super(StDepoLokasyon.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STDEPO_LOKASYON_TIPI -> {
                return filterOptionService.getStDepLokasyonTipis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
