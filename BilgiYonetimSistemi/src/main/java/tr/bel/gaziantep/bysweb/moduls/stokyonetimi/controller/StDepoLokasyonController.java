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
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoLokasyonService;

import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:25
 */
@Named
@ViewScoped
@Slf4j
public class StDepoLokasyonController extends AbstractController<StDepoLokasyon> {

    @Serial
    private static final long serialVersionUID = 3088507083220304712L;

    @Inject
    private StDepoLokasyonService service;
    @Inject
    private FilterOptionService filterOptionService;

    public StDepoLokasyonController() {
        super(StDepoLokasyon.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STDEPO_LOKASYON_TIPI -> {
                return filterOptionService.getStDepoLokasyonTipis();
            }
            case GNLGUN -> {
                return filterOptionService.getGnlGuns();
            }
            case AYGRUP -> {
                return filterOptionService.getAyGrups();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }


    /**
     * Ust lokasyon seciminde sadece ayni depoya ait ve kendisi olmayan kayitlar gosterilir.
     * Depo henuz secilmediyse tum lokasyonlar listelenir.
     */
    public List<StDepoLokasyon> getUstLokasyonItems() {
        List<StDepoLokasyon> tumLokasyonlar = service.findAll();
        if (getSelected() == null || getSelected().getStDepo() == null || getSelected().getStDepo().getId() == null) {
            return tumLokasyonlar;
        }
        Integer depoId = getSelected().getStDepo().getId();
        Integer selectedId = getSelected().getId();
        return tumLokasyonlar.stream()
                .filter(x -> x.getStDepo() != null && depoId.equals(x.getStDepo().getId()))
                .filter(x -> selectedId == null || !selectedId.equals(x.getId()))
                .collect(Collectors.toList());
    }

    /**
     * Depo degistiginde onceki depodan secilmis ust lokasyonu temizler.
     */
    public void onDepoChange() {
        if (getSelected() != null) {
            getSelected().setUstDepoLokasyon(null);
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (!ustLokasyonDepoUyumuGecerli()) {
            return;
        }
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (!ustLokasyonDepoUyumuGecerli()) {
            return;
        }
        super.saveNew(event);
    }

    private boolean ustLokasyonDepoUyumuGecerli() {
        if (getSelected() == null || getSelected().getUstDepoLokasyon() == null) {
            return true;
        }
        StDepoLokasyon ust = getSelected().getUstDepoLokasyon();
        if (getSelected().getStDepo() == null || ust.getStDepo() == null
                || !Objects.equals(getSelected().getStDepo().getId(), ust.getStDepo().getId())) {
            FacesUtil.addExclamationMessage("Üst lokasyon, seçilen depo ile ayný depoya ait olmalýdýr.");
            return false;
        }
        return true;
    }
}
