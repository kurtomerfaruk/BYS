package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStok;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtStokHareket;

import java.io.Serial;

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

    public OrtStokHareketController() {
        super(OrtStokHareket.class);
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
