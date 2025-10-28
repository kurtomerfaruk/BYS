package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmetAnket;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbVerilecekHizmet;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.07.2025 08:24
 */
@Named
@ViewScoped
@Slf4j
public class EdbHizmetAnketController extends AbstractController<EdbHizmetAnket> {

    @Serial
    private static final long serialVersionUID = -8059877716985475993L;

    @Inject
    private FilterOptionService filterOptionService;

    public EdbHizmetAnketController() {
        super(EdbHizmetAnket.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void secilenVerilecekHizmet(SelectEvent<EdbVerilecekHizmet> event) {
        EdbVerilecekHizmet verilecekHizmet = event.getObject();
        this.getSelected().setEdbVerilecekHizmet(verilecekHizmet);
    }
}
