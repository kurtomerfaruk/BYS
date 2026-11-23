package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoYetki;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoYetkiService;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 09:34
 */
@Named
@ViewScoped
@Slf4j
public class StDepoController extends AbstractController<StDepo> {

    @Serial
    private static final long serialVersionUID = 6269179155296421567L;

    @Inject
    private StDepoService service;
    @Inject
    private StDepoYetkiService depoYetkiService;


    public StDepoController() {
        super(StDepo.class);
    }

    @Override
    public Collection<StDepo> getItems() {
        EnumSyKullaniciTuru kullaniciTuru = this.getSyKullanici().getKullaniciTuru();
        if (kullaniciTuru.equals(EnumSyKullaniciTuru.SISTEM_YONETICISI)) {
            return service.findAll();
        }
        List<StDepoYetki> yetkis = depoYetkiService.findByKullanici(this.getSyKullanici());
        if (yetkis.isEmpty()) {
            return Collections.emptyList();
        }
        return yetkis.stream().map(StDepoYetki::getStDepo).filter(Objects::nonNull).distinct().toList();
    }

    public List<StDepo> all() {
        return service.findAll();
    }


}