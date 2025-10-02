package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 14:14
 */
@Named
@ViewScoped
@Slf4j
public class OrtBasvuruSecController extends AbstractController<OrtBasvuru> {

    @Serial
    private static final long serialVersionUID = 7705744810106122752L;

    public OrtBasvuruSecController() {
        super(OrtBasvuru.class);
    }

    @Override
    @PostConstruct
    public void init(){
        this.getFilterMap().put("basvuruDurumu", FilterMeta
                .builder()
                .field("basvuruDurumu")
                .filterValue(EnumOrtBasvuruDurumu.OLUMLU)
                .matchMode(MatchMode.EQUALS)
                .build());
        this.getFilterMap().put("basvuruHareketDurumu", FilterMeta
                .builder()
                .field("basvuruHareketDurumu")
                .filterValue(EnumOrtBasvuruHareketDurumu.OLCU_ICIN_RANDEVU_VERILDI)
                .matchMode(MatchMode.EQUALS)
                .build());
    }

    public void ortBasvuruSecKapat(OrtBasvuru basvuru) {
        PrimeFaces.current().dialog().closeDynamic(basvuru);
    }

    public void onRowDblSelect(SelectEvent<OrtBasvuru> event) {
        OrtBasvuru ortBasvuru = event.getObject();
        ortBasvuruSecKapat(ortBasvuru);
    }
}
