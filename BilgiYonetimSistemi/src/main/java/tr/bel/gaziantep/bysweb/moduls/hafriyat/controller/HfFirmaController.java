package tr.bel.gaziantep.bysweb.moduls.hafriyat.controller;

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
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfFirma;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.07.2025 16:31
 */
@Named
@ViewScoped
@Slf4j
public class HfFirmaController extends AbstractController<HfFirma> {

    @Serial
    private static final long serialVersionUID = 2377606000527736172L;

    @Inject
    private FilterOptionService filterOptionService;

    public HfFirmaController() {
        super(HfFirma.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case HFFIRMA_TURU -> {
                return filterOptionService.getHfFirmaTurus();
            }
            case EVET_HAYIR -> {
                return filterOptionService.getSyFilterAnahtars();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public HfFirma prepareCreate(ActionEvent event) {
        HfFirma newItem;
        try {
            newItem = HfFirma.class.getDeclaredConstructor().newInstance();
            newItem.setGnlIl(new GnlIl());
            newItem.setGnlIlce(new GnlIlce());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void hfFirmaSecKapat(HfFirma hfFirma) {
        PrimeFaces.current().dialog().closeDynamic(hfFirma);
    }

    public void onRowDblSelect(SelectEvent<HfFirma> event) {
        HfFirma hfFirma = event.getObject();
        hfFirmaSecKapat(hfFirma);
    }
}
