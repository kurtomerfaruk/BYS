package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:20
 */
@Named
@ViewScoped
@Slf4j
public class EyTalepController extends AbstractController<EyTalep> {
    @Serial
    private static final long serialVersionUID = 9114186622311189099L;

    @Inject
    private FilterOptionService filterOptionService;

    public EyTalepController() {
        super(EyTalep.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case TALEP_TURU -> {
                return filterOptionService.getGnlTalepTurus();
            }
            case TALEP_TIPI -> {
                return filterOptionService.getGnlTalepTipis();
            }
            case TALEP_DURUMU -> {
                return filterOptionService.getTalepDurumus();
            }
            case EYTALEP_KONU -> {
                return filterOptionService.getEyTalepKonus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public EyTalep prepareCreate(ActionEvent event) {
        EyTalep newItem;
        try {
            newItem = EyTalep.class.getDeclaredConstructor( ).newInstance();
            EyKisi eyKisi = new EyKisi();
            eyKisi.setGnlKisi(GnlKisi.builder().build());
            newItem.setEyKisi(eyKisi);
            newItem.setTarih(LocalDateTime.now());
            newItem.setDurum(EnumGnlTalepDurumu.BEKLIYOR);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        EyKisi eyKisi = event.getObject();
        this.getSelected().setEyKisi(eyKisi);
    }
}
