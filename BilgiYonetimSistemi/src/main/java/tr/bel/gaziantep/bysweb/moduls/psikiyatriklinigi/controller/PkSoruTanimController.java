package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkSoruTanim;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 2.10.2026 09:10
 */
@Named
@ViewScoped
@Slf4j
public class PkSoruTanimController extends AbstractController<PkSoruTanim> {

    @Serial
    private static final long serialVersionUID = -2077511566859376716L;

    @Inject
    private FilterOptionService filterOptionService;

    public PkSoruTanimController() {
        super(PkSoruTanim.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            case PKSORU_TUR->{
                return filterOptionService.getPkSoruTurs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
