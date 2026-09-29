package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkEgitimTur;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkEgitimKonu;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkEgitimKonuService;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 10:49
 */
@Named
@ViewScoped
@Slf4j
public class PkEgitimKonuController extends AbstractController<PkEgitimKonu> {

    @Serial
    private static final long serialVersionUID = 7238273645272506920L;

    @Inject
    private PkEgitimKonuService service;
    @Inject
    private FilterOptionService filterOptionService;

    public PkEgitimKonuController() {
        super(PkEgitimKonu.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case PKEGITIM_TUR -> {
                return filterOptionService.getPkEgitimTurs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public List<PkEgitimKonu> getEgitimKonuList(EnumPkEgitimTur tur){
        if(tur!=null){
            return service.findByTur(tur);
        }
        return Collections.emptyList();
    }
}
