package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.moralevi.EnumMeTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeTalep;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 11:18
 */
@Named
@ViewScoped
@Slf4j
public class MeTalepController extends AbstractController<MeTalep> {

    @Serial
    private static final long serialVersionUID = 8508042747050779879L;

    @Inject
    private FilterOptionService filterOptionService;

    public MeTalepController() {
        super(MeTalep.class);
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
            case METALEP_DURUMU -> {
                return filterOptionService.getMeTalepDurums();
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
    public MeTalep prepareCreate(ActionEvent event) {
        MeTalep newItem;
        try {
            newItem = MeTalep.class.getDeclaredConstructor( ).newInstance();
            EyKisi eyKisi = new EyKisi();
            eyKisi.setGnlKisi(GnlKisi.builder().build());
            newItem.setMeKisi(MeKisi.builder().eyKisi(eyKisi).build());
            newItem.setTarih(LocalDateTime.now());
            newItem.setDurum(EnumMeTalepDurumu.BEKLIYOR);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenMeKisi(SelectEvent<MeKisi> event) {
        MeKisi meKisi = event.getObject();
        this.getSelected().setMeKisi(meKisi);
    }
}
