package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBakiye;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokBarkodService;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:57
 */
@Named
@ViewScoped
@Slf4j
public class StStokController extends AbstractController<StStok> {

    @Serial
    private static final long serialVersionUID = -2934417348583578519L;

    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokHareketController stStokHareketController;

    @Getter
    @Setter
    private StStokBarkod barkod = new StStokBarkod();
    @Getter
    @Setter
    private BigDecimal toplamMiktar;
    @Getter
    @Setter
    private BigDecimal rezerveMiktar;
    @Getter
    @Setter
    private BigDecimal transferMiktar;

    public StStokController() {
        super(StStok.class);
    }

    @Override
    public StStok prepareCreate(ActionEvent event) {
        StStok newItem = super.prepareCreate(event);
        barkod = new StStokBarkod();
        return newItem;
    }

    public void addBarkod() {
        if (getSelected() == null) {
            return;
        }
        String barkodText = barkod.getBarkod();
        if (barkodText == null || barkodText.isBlank()) {
            FacesUtil.addExclamationMessage("Barkod zorunlu");
            return;
        }
        barkod.setBarkod(barkodText.trim());
        barkod.setStStok(getSelected());
        getSelected().getStokBarkodList().add(barkod);
        barkod = new StStokBarkod();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeBarkod(StStokBarkod item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getStokBarkodList().remove(item);
        if (item.getId() != null) {
            try {
                item.setAktif(false);
                stStokBarkodService.edit(item);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
        FacesUtil.successMessage(Constants.KAYIT_SILINDI);
    }

    /**
     * Stok karti icinden yeni hareket acar: hareket formunu secili stokla
     * on-doldurur ve mevcut Create dialog'unu gosterir.
     */
    public void prepareHareket() {
        if (getSelected() == null || getSelected().getId() == null) {
            FacesUtil.addExclamationMessage("stHareketStokZorunlu");
            return;
        }
        try {
            StStokHareket yeni = StStokHareket.class.getDeclaredConstructor().newInstance();
            yeni.setStStok(getSelected());
            yeni.setIslemTarihi(java.time.LocalDateTime.now());
            yeni.setYon(tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis.GIRIS);
            stStokHareketController.setSelected(yeni);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Stok karti icinden secilen hareketin tersini olusturur.
     */
    public void tersHareket(StStokHareket hareket) {
        if (hareket == null) {
            return;
        }
        stStokHareketController.setSelected(hareket);
        stStokHareketController.tersHareket();
    }

    public void editInfo() {
        if (this.getSelected() == null) return;
        toplamMiktar = this.getSelected().getStokBakiyeList().stream()
                .filter(BaseEntity::isAktif)
                .map(StStokBakiye::getMiktar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        rezerveMiktar = this.getSelected().getStokBakiyeList().stream()
                .filter(BaseEntity::isAktif)
                .map(StStokBakiye::getRezerveMiktar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        transferMiktar = this.getSelected().getStokBakiyeList().stream()
                .filter(BaseEntity::isAktif)
                .map(StStokBakiye::getTransferdekiMiktar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void saveProceed(ActionEvent event) {
        StStok oldStok = this.getSelected();
        saveNew(event);
        StStok stok = prepareCreate(event);
        stok.setStStokKategori(oldStok.getStStokKategori());
        stok.setStBirim(oldStok.getStBirim());
        stok.setKdvOrani(oldStok.getKdvOrani());
        String malzemeKodu = StringUtil.incrementAccountCode(oldStok.getMalzemeKodu());
        stok.setMalzemeKodu(malzemeKodu);
        stok.setMarka(oldStok.getMarka());
        this.setSelected(stok);
    }
}

