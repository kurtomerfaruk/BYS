package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyCihazDurum;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyArac;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:32
 */
@Named
@ViewScoped
@Slf4j
public class EyAracCihazTeslimiController extends AbstractController<EyAracCihazTeslimi> {

    @Serial
    private static final long serialVersionUID = 9203561728988451599L;

    @Inject
    private FilterOptionService filterOptionService;

    public EyAracCihazTeslimiController() {
        super(EyAracCihazTeslimi.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public EyAracCihazTeslimi prepareCreate(ActionEvent event) {
        EyAracCihazTeslimi newItem;
        try {
            newItem = EyAracCihazTeslimi.class.getDeclaredConstructor().newInstance();
            EyKisi eyKisi = EyKisi.builder().gnlKisi(new GnlKisi()).build();
            newItem.setEyKisi(eyKisi);
            newItem.setVerilisTarihi(LocalDateTime.now());
            newItem.setEyArac(new EyArac());
            newItem.setCihazDurum(EnumEyCihazDurum.VATANDASTA);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        EyKisi eyKisi = event.getObject();
        this.getSelected().setEyKisi(eyKisi);
    }
}
