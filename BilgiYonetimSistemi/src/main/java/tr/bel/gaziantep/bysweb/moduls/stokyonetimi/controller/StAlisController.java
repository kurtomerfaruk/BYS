package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStAlisBelgeTuru;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StAlis;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StAlisKalem;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StAlisService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:13
 */
@Named
@ViewScoped
@Slf4j
public class StAlisController extends AbstractController<StAlis> {

    @Serial
    private static final long serialVersionUID = 1202600117921L;

    @Inject
    private StAlisService alisService;
    @Inject
    private FilterOptionService filterOptionService;

    @lombok.Getter
    @lombok.Setter
    private StAlisKalem kalem = new StAlisKalem();

    public StAlisController() {
        super(StAlis.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STALIS_BELGE_TURU -> {
                return filterOptionService.getStAlisBelgeTurus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public StAlis prepareCreate(ActionEvent event) {
        StAlis yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setTarih(LocalDateTime.now());
            yeni.setBelgeTuru(EnumStAlisBelgeTuru.FATURA);
            yeni.setKalemler(new java.util.ArrayList<>());
        }
        kalem = new StAlisKalem();
        return yeni;
    }

    public void onDepoChange() {
        if (getSelected() != null) {
            getSelected().setStDepoLokasyon(null);
        }
    }

    public void addKalem() {
        if (getSelected() == null) {
            return;
        }
        if (kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            FacesUtil.addExclamationMessage("Alış kalemi için stok seçin");
            return;
        }
        if (kalem.getMiktar() == null || kalem.getMiktar().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            FacesUtil.addExclamationMessage("stAlisKalemMiktarPozitif");
            return;
        }
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new java.util.ArrayList<>());
        }
        kalem.setStAlis(getSelected());
        getSelected().getKalemler().add(kalem);
        kalem = new StAlisKalem();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeKalem(StAlisKalem item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getKalemler().remove(item);
        FacesUtil.successMessage(Constants.KAYIT_SILINDI);
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StAlis kayit = alisService.alisOlustur(getSelected(), this.getSyKullanici());
            setSelected(kayit);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Alis olusturulamadi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}

