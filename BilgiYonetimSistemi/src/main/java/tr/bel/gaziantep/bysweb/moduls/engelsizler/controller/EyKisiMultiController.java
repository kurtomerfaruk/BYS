package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.09.2025 08:37
 */
@Named
@ViewScoped
public class EyKisiMultiController extends AbstractController<EyKisi> {
    @Serial
    private static final long serialVersionUID = -1757675153556951307L;

    @Getter
    @Setter
    private List<EyKisi> selecteds;

    public EyKisiMultiController() {
        super(EyKisi.class);
    }

    @PostConstruct
    public void init() {
        this.getFilterMap().put("gnlKisi.durum", FilterMeta.builder()
                .field("gnlKisi.durum")
                .filterValue(Arrays.asList(EnumGnlDurum.OLUM,EnumGnlDurum.OLU, EnumGnlDurum.OLUMUN_TESPITI))
                .matchMode(MatchMode.NOT_IN)
                .build());
    }

    public void selectAndClose() {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedList", selecteds);
        PrimeFaces.current().dialog().closeDynamic(params);
    }
}
