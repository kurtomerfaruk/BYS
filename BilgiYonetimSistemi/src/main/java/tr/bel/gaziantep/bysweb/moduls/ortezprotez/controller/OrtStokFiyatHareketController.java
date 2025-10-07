package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokFiyatHareket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 09:03
 */
@Named
@ViewScoped
@Slf4j
public class OrtStokFiyatHareketController extends AbstractController<OrtStokFiyatHareket> {

    @Serial
    private static final long serialVersionUID = -8997261793390355526L;

    public OrtStokFiyatHareketController() {
        super(OrtStokFiyatHareket.class);
    }

    public void getInfo(OrtStok ortStok) {
        FilterMeta filterMeta = FilterMeta.builder()
                .field("ortStok.id")
                .filterValue(ortStok.getId())
                .matchMode(MatchMode.EQUALS)
                .build();
        this.getFilterMap().put("ortStok.id", filterMeta);
    }
}
