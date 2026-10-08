package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 25.09.2026 10:44
 */
@Named
@ViewScoped
@Slf4j
public class PkHastaTedaviSekliController extends AbstractController<PkHastaTedaviSekli> {

    @Serial
    private static final long serialVersionUID = -147202773805226019L;

    @Inject
    private FilterOptionService filterOptionService;

    public PkHastaTedaviSekliController() {
        super(PkHastaTedaviSekli.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case PKTEDAVI_SEKLI -> {
                return filterOptionService.getPkTedaviSeklis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public PkHastaTedaviSekli prepareCreate(ActionEvent event) {
        PkHastaTedaviSekli newItem;
        try {
            newItem = PkHastaTedaviSekli.class.getDeclaredConstructor().newInstance();
            newItem.setPkHasta(PkHasta.builder().gnlKisi(new GnlKisi()).build());
            newItem.setTarih(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkHasta(SelectEvent<PkHasta> event) {
        PkHasta pkHasta = event.getObject();
        this.getSelected().setPkHasta(pkHasta);
    }

    public void tedaviSekliSecKapat(PkHastaTedaviSekli pkHastaTedaviSekli) {
        PrimeFaces.current().dialog().closeDynamic(pkHastaTedaviSekli);
    }

    public void onRowDblSelect(SelectEvent<PkHastaTedaviSekli> event) {
        tedaviSekliSecKapat(event.getObject());
    }
}
