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
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoLokasyonService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokBakiyeService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokHareketService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    private StDepoLokasyonService stDepoLokasyonService;
    @Inject
    private StStokBakiyeService stStokBakiyeService;

    public StStokHareketController() {
        super(StStokHareket.class);
    }


    /**
     * Sekilen depoya ait lokasyonlar; depo secilmediyse bos liste.
     */
    public List<StDepoLokasyon> getLokasyonItems() {
        if (getSelected() == null || getSelected().getStDepo() == null
                || getSelected().getStDepo().getId() == null) {
            return Collections.emptyList();
        }
        Integer depoId = getSelected().getStDepo().getId();
        return stDepoLokasyonService.findAll().stream()
                .filter(x -> x.getStDepo() != null && depoId.equals(x.getStDepo().getId()))
                .collect(Collectors.toList());
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        if (key == EnumSyFiltreAnahtari.STHAREKET_TURU) {
            return Arrays.stream(EnumStHareketTuru.values())
                    .map(value -> new SelectItem(value, value.getDisplayValue()))
                    .collect(Collectors.toList());
        }
        if (key == EnumSyFiltreAnahtari.GIRIS_CIKIS) {
            return Arrays.stream(EnumGirisCikis.values())
                    .map(value -> new SelectItem(value, value.getDisplayValue()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
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
     * Depo degistiginde onceki depoya ait lokasyon secimini temizler.
     */
    public void onDepoChange() {
        if (getSelected() != null) {
            getSelected().setStDepoLokasyon(null);
        }
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
                        kayit.getStStok(), kayit.getStDepo(), kayit.getStDepoLokasyon(),
                        kayit.getLot(), kayit.getSkt(), false);
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
