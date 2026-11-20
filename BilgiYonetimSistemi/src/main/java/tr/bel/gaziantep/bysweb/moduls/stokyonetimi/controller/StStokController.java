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
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.*;

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
    private static final String TOPLAM_KOLON = "__TOPLAM__";

    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokHareketController stStokHareketController;
    @Inject
    private StDepoYetkiService stDepoYetkiService;
    @Inject
    private StDepoService stDepoService;
    @Inject
    private StStokBakiyeService stStokBakiyeService;

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

    private List<StDepo> yetkiliDepolar;
    private Map<Integer, Map<Integer, BigDecimal>> depoMiktarMap = new HashMap<>();

    public StStokController() {
        super(StStok.class);
    }

    /**
     * Kullaniciya gore miktar kolonlarinda gosterilecek depolar.
     * Sistem yoneticisi tum aktif depoları, diger kullanici yetkili oldugu depoları gorur.
     */
    public List<StDepo> getYetkiliDepolar() {
        if (yetkiliDepolar == null) {
            SyKullanici kullanici = getSyKullanici();
            if (kullanici != null && kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
                yetkiliDepolar = stDepoService.findAll();
            } else {
                List<StDepoYetki> yetkiler = stDepoYetkiService.findByKullanici(kullanici);
                yetkiliDepolar = yetkiler == null ? List.of()
                        : yetkiler.stream()
                        .map(StDepoYetki::getStDepo)
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList();
            }
        }
        return yetkiliDepolar;
    }

    /**
     * Liste sayfası yuklendiginde o sayfadaki stoklarin yetkili depo bakiyelerini tek sorguda hazirlar.
     */
    @Override
    protected void afterLoad(List<StStok> result) {
        yukleDepoMiktarlari(result);
    }

    private void yukleDepoMiktarlari(List<StStok> result) {
        depoMiktarMap = new HashMap<>();
        List<StDepo> depolar = getYetkiliDepolar();
        if (result == null || result.isEmpty() || depolar.isEmpty()) {
            return;
        }
        List<Integer> stokIds = result.stream()
                .map(StStok::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        List<Integer> depoIds = depolar.stream()
                .map(StDepo::getId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (stokIds.isEmpty() || depoIds.isEmpty()) {
            return;
        }
        List<StStokBakiye> bakiyeler = stStokBakiyeService.findByStokAndDepos(stokIds, depoIds);
        for (StStokBakiye bakiye : bakiyeler) {
            if (bakiye.getStStok() == null || bakiye.getStDepo() == null) {
                continue;
            }
            BigDecimal miktar = bakiye.getMiktar() == null ? BigDecimal.ZERO : bakiye.getMiktar();
            depoMiktarMap.computeIfAbsent(bakiye.getStStok().getId(), k -> new HashMap<>())
                    .merge(bakiye.getStDepo().getId(), miktar, BigDecimal::add);
        }
    }

    public BigDecimal getDepoMiktar(StStok stok, StDepo depo) {
        if (stok == null || depo == null || stok.getId() == null || depo.getId() == null) {
            return BigDecimal.ZERO;
        }
        Map<Integer, BigDecimal> stokBakiyeleri = depoMiktarMap.get(stok.getId());
        return stokBakiyeleri == null ? BigDecimal.ZERO
                : stokBakiyeleri.getOrDefault(depo.getId(), BigDecimal.ZERO);
    }

    public BigDecimal getToplamDepoMiktar(StStok stok) {
        BigDecimal toplam = BigDecimal.ZERO;
        for (StDepo depo : getYetkiliDepolar()) {
            toplam = toplam.add(getDepoMiktar(stok, depo));
        }
        return toplam;
    }

    @Override
    public List<?> getExtraColumns() {
        List<Object> kolonlar = new ArrayList<>(getYetkiliDepolar());
        kolonlar.add(TOPLAM_KOLON);
        return kolonlar;
    }

    @Override
    public String getExtraColumnHeader(Object column) {
        if (TOPLAM_KOLON.equals(column)) {
            return "Toplam Miktar";
        }
        if (column instanceof StDepo depo) {
            return depo.getAd();
        }
        return super.getExtraColumnHeader(column);
    }

    /**
     * Siralama/filtre ICIN kullanilan alan adi. Toplam kolonu hesaplanan deger oldugu icin
     * siralanamaz/filtrelenemez; bos donulur.
     */
    public String getExtraColumnField(Object column) {
        if (column instanceof StDepo depo && depo.getId() != null) {
            String field = StStokService.depoMiktarField(depo.getId());
            getFilter(field);
            return field;
        }
        return "";
    }

    @Override
    public Object getExtraColumnValue(Object item, Object column) {
        if (!(item instanceof StStok stok)) {
            return null;
        }
        if (TOPLAM_KOLON.equals(column)) {
            return getToplamDepoMiktar(stok);
        }
        if (column instanceof StDepo depo) {
            return getDepoMiktar(stok, depo);
        }
        return null;
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

