package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkSoruTur;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 2.10.2026 09:10
 */
@Named
@ViewScoped
@Slf4j
public class PkSoruTurController extends AbstractController<PkSoruTur> {

    @Serial
    private static final long serialVersionUID = -8372212706979429952L;

    @Inject
    private FilterOptionService filterOptionService;

    public PkSoruTurController() {
        super(PkSoruTur.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            case PKMODUL -> {
                return filterOptionService.getPkModuls();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void saveProceed(ActionEvent event) {
        PkSoruTur oldSoruTur = this.getSelected();
        saveNew(event);
        PkSoruTur soruTur = prepareCreate(event);
        soruTur.setModul(oldSoruTur.getModul());
        soruTur.setCokluSecim(oldSoruTur.isCokluSecim());
        soruTur.setSiraNo(oldSoruTur.getSiraNo() + 1);
        this.setSelected(soruTur);
    }
}
