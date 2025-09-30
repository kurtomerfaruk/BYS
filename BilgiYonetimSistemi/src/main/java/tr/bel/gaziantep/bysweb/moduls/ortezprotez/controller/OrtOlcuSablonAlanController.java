package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablonAlan;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtOlcuSablonAlanService;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.09.2025 14:48
 */
@Named
@ViewScoped
@Slf4j
public class OrtOlcuSablonAlanController extends AbstractController<OrtOlcuSablonAlan> {

    @Serial
    private static final long serialVersionUID = 3372004433316853001L;

    @Inject
    private OrtOlcuSablonAlanService service;
    @Inject
    private FilterOptionService filterOptionService;

    public OrtOlcuSablonAlanController() {
        super(OrtOlcuSablonAlan.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTOLCU_SABLON -> {
                return filterOptionService.getOrtOlcuSablons();
            }
            case ORTOLCU_SABLON_ALAN_TURU -> {
                return filterOptionService.getOrtOlcuSAblonAlanTurus();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    public void updateXY() {
        try {
            Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
            BigDecimal x = new BigDecimal(params.get("x"));
            BigDecimal y = new BigDecimal(params.get("y"));
            this.getSelected().setX(x);
            this.getSelected().setY(y);
        } catch (Exception e) {
            log.error(e.getMessage());
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
    public void updateWH() {
        try {
            BigDecimal width = new BigDecimal(Util.getParameter("width"));
            BigDecimal height = new BigDecimal(Util.getParameter("height"));
            this.getSelected().setGenislik(width);
            this.getSelected().setYukseklik(height);
        } catch (Exception e) {
            log.error(e.getMessage());
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void copy(ActionEvent event){
        OrtOlcuSablonAlan selected = this.getSelected();
        if (selected == null) {
            return;
        }
        try {
            service.copy(selected);
            FacesUtil.successMessage("kayitKopyalandi");
        } catch (Exception e) {
            log.error("Kopyalama hatası", e);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
