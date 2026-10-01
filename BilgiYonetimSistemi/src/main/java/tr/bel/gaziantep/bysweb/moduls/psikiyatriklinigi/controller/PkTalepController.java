package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

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
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkTalep;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 30.09.2026 08:36
 */
@Named
@ViewScoped
@Slf4j
public class PkTalepController extends AbstractController<PkTalep> {

    @Serial
    private static final long serialVersionUID = -5755013442879507857L;

    @Inject
    private FilterOptionService filterOptionService;

    public PkTalepController() {
        super(PkTalep.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case PKTALEP_TURU -> {
                return filterOptionService.getPkTalepTurus();
            }
            case TALEP_DURUMU -> {
                return filterOptionService.getTalepDurumus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public PkTalep prepareCreate(ActionEvent event) {
        PkTalep newItem;
        try {
            newItem = PkTalep.class.getDeclaredConstructor().newInstance();
            newItem.setTarih(LocalDateTime.now());
            newItem.setDurum(EnumGnlTalepDurumu.BEKLIYOR);
            newItem.setPkAile(PkAile.builder().gnlKisi(new GnlKisi()).build());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkAile(SelectEvent<PkAile> event) {
        PkAile pkAile = event.getObject();
        this.getSelected().setPkAile(pkAile);
    }
}
