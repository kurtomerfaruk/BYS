package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;

import java.io.Serial;
import java.util.Arrays;
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
public class EyAracCihazTeslimiVefatEdenController extends AbstractController<EyAracCihazTeslimi> {

    @Serial
    private static final long serialVersionUID = 9203561728988451599L;

    @Inject
    private FilterOptionService filterOptionService;

    public EyAracCihazTeslimiVefatEdenController() {
        super(EyAracCihazTeslimi.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case EYARAC_CIHAZ_DURUMU -> {
                return filterOptionService.getEyCihazDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @PostConstruct
    @Override
    public void init(){
        this.setTableName("EyAracCihazTeslimiVefatEden");
        readColumns();
        this.getFilterMap().put("eyKisi.gnlKisi.durum", FilterMeta.builder()
                .field("eyKisi.gnlKisi.durum")
                .filterValue(Arrays.asList(EnumGnlDurum.OLUM,EnumGnlDurum.OLU, EnumGnlDurum.OLUMUN_TESPITI))
                .matchMode(MatchMode.IN)
                .build());
    }

}
