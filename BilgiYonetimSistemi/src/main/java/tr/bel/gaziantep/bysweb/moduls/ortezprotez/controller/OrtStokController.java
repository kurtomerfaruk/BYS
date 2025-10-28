package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumAlisSatis;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtStokService;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 13:58
 */
@Named
@ViewScoped
@Slf4j
public class OrtStokController extends AbstractController<OrtStok> {

    @Serial
    private static final long serialVersionUID = -3373843224409827700L;

    @Inject
    private OrtStokService service;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private EnumAlisSatis fiyatTur;
    @Getter
    @Setter
    private BigDecimal fiyat;

    public OrtStokController() {
        super(OrtStok.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTSTOK_BIRIM -> {
                return filterOptionService.getOrtStokBirims();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void stokSecKapat(OrtStok ortStok) {
        PrimeFaces.current().dialog().closeDynamic(ortStok);
    }

    public void onRowDblSelect(SelectEvent<OrtStok> event) {
        OrtStok ortStok = event.getObject();
        stokSecKapat(ortStok);
    }

    public void preparePrice(){
        if (this.getSelected() != null) {
            fiyat = this.getSelected().getSatisFiyati() == null ? BigDecimal.ZERO : this.getSelected().getSatisFiyati();
            fiyatTur = EnumAlisSatis.SATIS;
        }
    }

    public void updatePrice(){
        try {
            service.updatePrice(this.getSelected(),fiyatTur,fiyat);
            FacesUtil.successMessage("fiyatGuncellendi");
        }catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
