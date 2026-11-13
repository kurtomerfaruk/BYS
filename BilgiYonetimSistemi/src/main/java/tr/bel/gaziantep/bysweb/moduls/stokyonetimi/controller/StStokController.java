package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 11:54
 */
@Named
@ViewScoped
@Slf4j
public class StStokController extends AbstractController<StStok> {

    @Serial
    private static final long serialVersionUID = 4518147386728773577L;


    @Getter
    @Setter
    private String barcode;
    @Getter
    @Setter
    private List<FilterMeta> filterBy;

    public StStokController() {
        super(StStok.class);
    }

    @PostConstruct
    @Override
    public void init() {
        super.init();
        filterBy = new ArrayList<>();
        filterBy.add(FilterMeta.builder()
                .field("aktif")
                .filterValue(true)
                .matchMode(MatchMode.EQUALS)
                .build());
    }

    public void addBarkod() {
        if (this.getSelected() == null) return;
        if (barcode == null || barcode.trim().isEmpty()) return;
        StStokBarkod barkod = new StStokBarkod();
        barkod.setStStok(this.getSelected());
        barkod.setBarkod(barcode.trim());
        if (this.getSelected().getStokBarkodList() == null) {
            this.getSelected().setStokBarkodList(new java.util.ArrayList<>());
        }
        this.getSelected().getStokBarkodList().add(barkod);
        barcode = null;
    }

    public void removeBarkod(StStokBarkod barkod) {
        if (barkod == null) return;
        barkod.setAktif(false);
        if (this.getSelected() != null && this.getSelected().getStokBarkodList() != null) {
//            this.getSelected().getStokBarkodList().remove(barkod);
            int index = this.getSelected().getStokBarkodList().indexOf(barkod);
            this.getSelected().getStokBarkodList().set(index, barkod);

        }
    }
}
