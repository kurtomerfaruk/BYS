package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbDurum;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbVerilecekHizmet;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.07.2025 09:56
 */
@Named
@ViewScoped
@Slf4j
public class EdbVerilecekHizmetSecController extends AbstractController<EdbVerilecekHizmet> {
    @Serial
    private static final long serialVersionUID = -6320191460358075608L;

    public EdbVerilecekHizmetSecController() {
        super(EdbVerilecekHizmet.class);
    }

    @Override
    @PostConstruct
    public void init(){
        this.getFilterMap().put("durum", FilterMeta
                .builder()
                .field("durum")
                .filterValue(EnumEdbDurum.TAMAMLANDI)
                .matchMode(MatchMode.EQUALS)
                .build());
    }

    public void edbVerilecekHizmetSecKapat(EdbVerilecekHizmet hizmet) {
        PrimeFaces.current().dialog().closeDynamic(hizmet);
    }

    public void onRowDblSelect(SelectEvent<EdbVerilecekHizmet> event) {
        EdbVerilecekHizmet verilecekHizmet = event.getObject();
        edbVerilecekHizmetSecKapat(verilecekHizmet);
    }

}
