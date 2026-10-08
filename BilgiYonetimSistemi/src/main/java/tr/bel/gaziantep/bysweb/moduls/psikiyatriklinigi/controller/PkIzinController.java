package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlIzinTuru;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkIzin;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkPersonel;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 30.09.2026 11:57
 */
@Named
@ViewScoped
@Slf4j
public class PkIzinController extends AbstractController<PkIzin> {

    @Serial
    private static final long serialVersionUID = -1765609260954574661L;

    @Inject
    private FilterOptionService filterOptionService;

    public PkIzinController() {
        super(PkIzin.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case IZIN_TURU -> {
                return filterOptionService.getEvetHayirs();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public PkIzin prepareCreate(ActionEvent event) {
        PkIzin newItem;
        try {
            newItem = PkIzin.class.getDeclaredConstructor().newInstance();
            newItem.setIzinBaslangic(LocalDate.now());
            newItem.setPkPersonel(new PkPersonel());
            newItem.setIzinTuru(EnumGnlIzinTuru.YILLIK);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getDayCount() {
        if (this.getSelected() == null) return;
        if (this.getSelected().getIzinBitis().isBefore(this.getSelected().getIzinBaslangic())) return;
        try {
            long gunSayisi = ChronoUnit.DAYS.between(this.getSelected().getIzinBaslangic(), this.getSelected().getIzinBitis());
            this.getSelected().setGunSayi(Integer.parseInt(gunSayisi + ""));
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.addErrorMessage("Gün hesaplaması yapılırken hata oluştu");
        }

    }
}
