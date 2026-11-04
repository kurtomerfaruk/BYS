package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtCihazTur;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtCihaz;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtCihazService;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 3.11.2026 13:51
 */
@Named
@ViewScoped
@Slf4j
public class OrtCihazController extends AbstractController<OrtCihaz> {

    @Serial
    private static final long serialVersionUID = 861317631639134299L;

    @Inject
    private OrtCihazService service;
    @Inject
    private FilterOptionService filterOptionService;

    public OrtCihazController() {
        super(OrtCihaz.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTCIHAZ_TURU -> {
                return filterOptionService.getOrtCihazTurs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public List<OrtCihaz> getCihazList(EnumOrtCihazTur cihazTur){
        if(cihazTur==null) return Collections.emptyList();
        return service.findByCihazTur(cihazTur);
    }
}
