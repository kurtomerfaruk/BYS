package tr.bel.gaziantep.bysweb.moduls.hafriyat.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfArac;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfDepolamaTesisiIs;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfFirma;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:45
 */
@Named
@ViewScoped
@Slf4j
public class HfDepolamaTesisiIsController extends AbstractController<HfDepolamaTesisiIs> {

    @Serial
    private static final long serialVersionUID = 8349587308787568821L;

    @Inject
    private FilterOptionService filterOptionService;

    public HfDepolamaTesisiIsController() {
        super(HfDepolamaTesisiIs.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case HFMAL_CINSI -> {
                return filterOptionService.getHfMalCinsis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public HfDepolamaTesisiIs prepareCreate(ActionEvent event) {
        HfDepolamaTesisiIs newItem;
        try {
            newItem = HfDepolamaTesisiIs.class.getDeclaredConstructor().newInstance();
            newItem.setHfArac(new HfArac());
            newItem.setHfFirma(new HfFirma());
            newItem.setTarih(LocalDateTime.now());
           this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenHfFirma(SelectEvent<HfFirma> event) {
        HfFirma hfFirma = event.getObject();
        this.getSelected().setHfFirma(hfFirma);
    }

    public void secilenHfArac(SelectEvent<HfArac> event) {
        HfArac hfArac = event.getObject();
        this.getSelected().setHfArac(hfArac);
    }

    public void calculate() {
        try {
            if (this.getSelected().getMiktar() == null) {
                this.getSelected().setMiktar(BigDecimal.ZERO);
            }

            if (this.getSelected().getFiyat() == null) {
                this.getSelected().setFiyat(BigDecimal.ZERO);
            }

            if (this.getSelected().getTutar() == null) {
                this.getSelected().setTutar(BigDecimal.ZERO);
            }

            BigDecimal result = this.getSelected().getFiyat().multiply(this.getSelected().getMiktar());
            result = result.setScale(3, RoundingMode.CEILING);
            this.getSelected().setTutar(result);
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }

    }
}
