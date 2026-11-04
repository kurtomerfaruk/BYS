package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtStokHareketTablo;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokHareket;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtStokHareketService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 16:08
 */
@Named
@ViewScoped
@Slf4j
public class OrtStokHareketController extends AbstractController<OrtStokHareket> {

    @Serial
    private static final long serialVersionUID = 4512967370114555782L;

    @Inject
    private OrtStokHareketService service;

    public OrtStokHareketController() {
        super(OrtStokHareket.class);
    }

    public OrtStokHareket prepare(OrtStok stok) {
        OrtStokHareket newItem;
        try {
            newItem = OrtStokHareket.class.getDeclaredConstructor().newInstance();
            newItem.setTarih(LocalDateTime.now());
            newItem.setOrtStok(stok);
            newItem.setTablo(EnumOrtStokHareketTablo.ORTSTOK_HAREKET);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getInfo(OrtStok ortStok) {
        FilterMeta filterMeta = FilterMeta.builder()
                .field("ortStok.id")
                .filterValue(ortStok.getId())
                .matchMode(MatchMode.EQUALS)
                .build();
        this.getFilterMap().put("ortStok.id", filterMeta);
    }

    @Override
    public void save(ActionEvent event){
        if(this.getSelected()==null) return;
        try {
            service.update(this.getSelected());
            FacesUtil.addSuccessMessage(this.getSelected().getDurum().getDisplayValue()+" eklendi");
        } catch (Exception e) {
            log.error(null,e);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void editInfo(OrtStokHareket ortStokHareket){
        try {
            this.setSelected(ortStokHareket);
        }catch (Exception ex){
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
