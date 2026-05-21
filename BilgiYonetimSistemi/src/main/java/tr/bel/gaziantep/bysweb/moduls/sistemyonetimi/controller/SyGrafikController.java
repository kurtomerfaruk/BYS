package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGrafik;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.04.2026 10:38
 */
@Named
@ViewScoped
@Slf4j
public class SyGrafikController extends AbstractController<SyGrafik> {

    @Serial
    private static final long serialVersionUID = -7440491994913858454L;

    @Inject
    private FilterOptionService filterOptionService;

    public SyGrafikController() {
        super(SyGrafik.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case MODUL -> {
                return filterOptionService.getModuls();
            }
            case GRAFIK_TURU -> {
                return filterOptionService.getGrafikTurs();
            }
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }
}
