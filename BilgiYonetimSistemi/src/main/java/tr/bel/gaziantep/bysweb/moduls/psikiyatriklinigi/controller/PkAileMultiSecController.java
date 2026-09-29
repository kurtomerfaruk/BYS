package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;

import java.io.Serial;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ortak "coklu aile secme" diyalogunun kontrolcusu.
 * pkAileMultiSec.xhtml sayfasinda kullanilir.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 29.09.2026 13:59
 */
@Named
@ViewScoped
@Slf4j
public class PkAileMultiSecController extends AbstractController<PkAile> {
    @Serial
    private static final long serialVersionUID = 4745244749881159791L;

    @Getter
    @Setter
    private List<PkAile> selecteds;

    public PkAileMultiSecController() {
        super(PkAile.class);
    }

    @PostConstruct
    @Override
    public void init() {
    }

    public void selectAndClose() {
        Map<String, Object> params = new HashMap<>();
        params.put("selectedList", selecteds);
        PrimeFaces.current().dialog().closeDynamic(params);
    }
}
