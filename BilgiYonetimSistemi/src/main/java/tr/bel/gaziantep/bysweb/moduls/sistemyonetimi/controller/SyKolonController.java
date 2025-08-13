package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreEslesmeModu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreTuru;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKolon;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 15:47
 */
@Named
@ViewScoped
@Slf4j
public class SyKolonController extends AbstractController<SyKolon> {

    @Serial
    private static final long serialVersionUID = 2399087270783148594L;

    @Inject
    private FilterOptionService filterOptionService;

    public SyKolonController() {
        super(SyKolon.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case FILTRE_TURU -> {
                return filterOptionService.getSyFilterTurs();
            }
            case FILTRE_ANAHTARI -> {
                return filterOptionService.getSyFilterAnahtars();
            }
            case VERI_TIPI -> {
                return filterOptionService.getSyVeriTips();
            }
            case FILTRE_ESLESTIRME_MODU -> {
                return filterOptionService.getSyFiltreEslestirmeMods();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    public SyKolon prepareCreate(ActionEvent event) {
        SyKolon newItem;
        try {
            newItem = SyKolon.class.getDeclaredConstructor().newInstance();
            newItem.setFiltreEslesmeModu(EnumSyFiltreEslesmeModu.STARTS_WITH);
            newItem.setFiltreTuru(EnumSyFiltreTuru.INPUT);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void saveProceed(ActionEvent event) {
        SyKolon oldKolon = this.getSelected();
        saveNew(event);
        SyKolon kolon = prepareCreate(event);
        kolon.setSyTablo(oldKolon.getSyTablo());
        this.setSelected(kolon);
    }

    public void updateColumn(){
        try {
            if(this.getSelected()==null) return;
            this.getSelected().setSortProperty(this.getSelected().getColumnProperty());
            this.getSelected().setFilterProperty(this.getSelected().getColumnProperty());
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void updateFilterMatchMode(){
        if(this.getSelected()==null) return;
        try {
            EnumSyFiltreTuru filtreTuru= this.getSelected().getFiltreTuru();
            if(filtreTuru  == EnumSyFiltreTuru.DATE_PICKER){
                this.getSelected().setTip(EnumSyVeriTipi.DATE_TIME);
                this.getSelected().setFiltreEslesmeModu(EnumSyFiltreEslesmeModu.BETWEEN);
            }
            EnumSyVeriTipi veriTipi=this.getSelected().getTip();
            this.getSelected().setFiltreEslesmeModu(setFilterMatchMode(veriTipi));
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private EnumSyFiltreEslesmeModu setFilterMatchMode(EnumSyVeriTipi veriTipi) {
        if(veriTipi==null) return EnumSyFiltreEslesmeModu.STARTS_WITH;
        switch (veriTipi) {
            case STRING -> {
                return EnumSyFiltreEslesmeModu.CONTAINS;
            }
            case ENUM, BOOLEAN -> {
                return EnumSyFiltreEslesmeModu.EQUALS;
            }
            case DATE, DATE_TIME -> {
                return EnumSyFiltreEslesmeModu.BETWEEN;
            }
            default -> {
                return EnumSyFiltreEslesmeModu.STARTS_WITH;
            }
        }
    }
}
