package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 08:49
 */
@Named
@ViewScoped
@Slf4j
public class StStokHareketController extends AbstractController<StStokHareket> {

    @Serial
    private static final long serialVersionUID = -6610720030443546883L;

    @Inject
    private FilterOptionService filterOptionService;

    public StStokHareketController() {
        super(StStokHareket.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STHAREKET_TURU -> {
                return filterOptionService.getStHareketTurus();
            }
            case GIRIS_CIKIS -> {
                return filterOptionService.getGirisCikis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public StStokHareket prepareCreate(ActionEvent event) {
        StStokHareket hareket = super.prepareCreate(event);
        if (hareket != null) {
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setIptalEdildi(false);
            hareket.setSyKullanici(this.getSyKullanici());
        }
        return hareket;
    }
}
