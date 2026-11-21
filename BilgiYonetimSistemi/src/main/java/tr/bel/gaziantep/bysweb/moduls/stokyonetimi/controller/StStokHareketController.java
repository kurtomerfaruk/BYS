package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStHareketTuru;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokBakiyeService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokHareketService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 11:57
 */
@Named
@ViewScoped
@Slf4j
public class StStokHareketController extends AbstractController<StStokHareket> {

    @Serial
    private static final long serialVersionUID = -203651086123371717L;

    @Inject
    private StStokHareketService hareketService;
    @Inject
    private StStokBakiyeService stStokBakiyeService;
    @Inject
    private FilterOptionService filterOptionService;

    public StStokHareketController() {
        super(StStokHareket.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STHAREKET_TURU -> {
                return filterOptionService.getStHareketTurs();
            }
            case GIRIS_CIKIS -> {
                return filterOptionService.getGirisCikis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public List<SelectItem> getManualHareketTuruOptions() {
        return Arrays.stream(EnumStHareketTuru.values())
                .filter(t -> !EnumStHareketTuru.SATIN_ALMA.equals(t))
                .filter(t -> !EnumStHareketTuru.SATIS.equals(t))
                .filter(t -> !EnumStHareketTuru.TRANSFER_GIRIS.equals(t))
                .filter(t -> !EnumStHareketTuru.TRANSFER_CIKIS.equals(t))
                .filter(t -> !EnumStHareketTuru.SAYIM_FARKI.equals(t))
                .map(t -> new SelectItem(t, t.getDisplayValue()))
                .toList();
    }

    @Override
    public StStokHareket prepareCreate(ActionEvent event) {
        StStokHareket yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setIslemTarihi(LocalDateTime.now());
            yeni.setYon(EnumGirisCikis.GIRIS);
        }
        return yeni;
    }

    /**
     * Hareket turu secildiginde yonu turun varsayilan degerine ceker.
     */
    public void onHareketTuruChange() {
        if (getSelected() == null) {
            return;
        }
        EnumGirisCikis varsayilan = hareketService.varsayilanYon(getSelected().getHareketTuru());
        if (varsayilan != null) {
            getSelected().setYon(varsayilan);
        }
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            StStokHareket kayit = hareketService.hareketEkle(getSelected());
            setSelected(kayit);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            // kritik stok uyarisi
            try {
                StStokBakiye bakiye = stStokBakiyeService.find(
                        kayit.getStStok(), kayit.getStDepo(), kayit.getSkt(), false);
                if (bakiye != null && kayit.getStStok().getKritikStok() != null
                        && bakiye.getMiktar().compareTo(kayit.getStStok().getKritikStok()) <= 0) {
                    FacesUtil.addExclamationMessage("Kritik stok seviyesine ulaştığından / altına düşülemez");
                }
            } catch (Exception ignored) {
            }
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Stok hareketi eklenemedi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Secili hareketin tersini olusturur (insert-only duzeltme).
     */
    public void tersHareket() {
        try {
            if (getSelected() == null) {
                FacesUtil.warningMessage("İşlem için bir hareket seçin");
                return;
            }
            StStokHareket ters = hareketService.tersHareketOlustur(getSelected());
            setSelected(ters);
            FacesUtil.addSuccessMessage("Ters hareket oluşturuldu");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Ters hareket olusturulamadi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
