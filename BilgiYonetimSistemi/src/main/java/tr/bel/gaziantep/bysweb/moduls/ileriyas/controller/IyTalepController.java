package tr.bel.gaziantep.bysweb.moduls.ileriyas.controller;

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
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyTalep;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.11.0
 * @since 15.06.2026 09:02
 */
@Named
@ViewScoped
@Slf4j
public class IyTalepController extends AbstractController<IyTalep> {

    @Serial
    private static final long serialVersionUID = -4658774530923640683L;

    @Inject
    private FilterOptionService filterOptionService;

    public IyTalepController() {
        super(IyTalep.class);
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
            case IYTALEP_KONU -> {
                return filterOptionService.getIyTalepKonus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public IyTalep prepareCreate(ActionEvent event) {
        IyTalep newItem;
        try {
            newItem = IyTalep.class.getDeclaredConstructor( ).newInstance();
            IyKisi iyKisi = new IyKisi();
            iyKisi.setGnlKisi(GnlKisi.builder().build());
            newItem.setIyKisi(iyKisi);
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

    public void secilenIyKisi(SelectEvent<IyKisi> event) {
        IyKisi iyKisi = event.getObject();
        this.getSelected().setIyKisi(iyKisi);
    }
}
