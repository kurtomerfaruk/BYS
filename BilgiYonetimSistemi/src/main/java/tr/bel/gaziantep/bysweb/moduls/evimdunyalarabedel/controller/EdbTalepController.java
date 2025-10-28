package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.faces.event.ActionEvent;
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
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmet;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbTalep;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service.EdbHizmetService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 15:54
 */
@Named
@ViewScoped
@Slf4j
public class EdbTalepController extends AbstractController<EdbTalep> {

    @Serial
    private static final long serialVersionUID = 6492578866259297532L;

    @Inject
    private EdbHizmetService hizmetService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private EdbTalep edbTalep;
    @Getter
    @Setter
    private EnumEdbBasvuruDurumu talepDurumu;

    public EdbTalepController() {
        super(EdbTalep.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case KISI_DURUM -> {
                return filterOptionService.getGnlKisiDurums();
            }
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case TALEP_TIPI -> {
                return filterOptionService.getGnlTalepTipis();
            }
            case TALEP_TURU -> {
                return filterOptionService.getGnlTalepTurus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public EdbTalep prepareCreate(ActionEvent event) {
        EdbTalep newItem;
        try {
            newItem = EdbTalep.class.getDeclaredConstructor().newInstance();
            if (edbTalep != null) {
                newItem.setEdbBasvuru(edbTalep.getEdbBasvuru());
                newItem.setTarih(edbTalep.getTarih());
                newItem.setTalepTipi(edbTalep.getTalepTipi());
                newItem.setTalepTuru(edbTalep.getTalepTuru());
            } else {
                newItem.setTarih(LocalDateTime.now());
                EdbBasvuru basvuru = EdbBasvuru.builder().eyKisi(new EyKisi()).build();
                newItem.setEdbBasvuru(basvuru);
            }
            newItem.setTalepDurumu(EnumEdbBasvuruDurumu.ON_KAYIT);
            newItem.setPlanlamaYapildi(Boolean.FALSE);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void saveProceed(ActionEvent event) {
        if (this.getSelected() != null) {
            edbTalep = this.getSelected();
            this.saveNew(event);
            prepareCreate(event);
        }
    }

    public void secilenBasvuru(SelectEvent<EdbBasvuru> event) {
        EdbBasvuru basvuru = event.getObject();
        this.getSelected().setEdbBasvuru(basvuru);
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            talepDurumu = this.getSelected().getTalepDurumu();
        }
    }

    public List<EdbHizmet> getHizmetList() {
        if (this.getSelected() != null) {
            if (this.getSelected().getTalepEdilenHizmetler() != null) {
                return hizmetService.findByHizmetTuru(this.getSelected().getTalepEdilenHizmetler());
            }
        }
        return null;
    }

    public void onRowDblSelect(SelectEvent<EdbTalep> event) {
        EdbTalep edbTalep = event.getObject();
        edbTalepSecKapat(edbTalep);
    }

    public void edbTalepSecKapat(EdbTalep edbTalep) {
        PrimeFaces.current().dialog().closeDynamic(edbTalep);
    }
}
