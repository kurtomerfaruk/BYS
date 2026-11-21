package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokService stStokService;
    @Inject
    private StDepoYetkiService stDepoYetkiService;
    @Inject
    private StDepoService stDepoService;
    @Inject
    private StStokController stStokController;

    @Getter
    @Setter
    private StAlisKalem kalem = new StAlisKalem();

    @Getter
    @Setter
    private String barkodOku;
    @Getter
    @Setter
    private BigDecimal alisMiktar = BigDecimal.ONE;
    @Getter
    @Setter
    private BigDecimal alisBirimMaliyet;
    @Getter
    @Setter
    private boolean barkodBulunamadi;

    private StDepo sonDepo;
    private StTedarikci sonTedarikci;
    private List<StDepo> yetkiliDepolar;

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

    public List<StDepo> getYetkiliDepolar() {
        if (yetkiliDepolar == null) {
            SyKullanici kullanici = getSyKullanici();
            if (kullanici != null && kullanici.getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
                yetkiliDepolar = stDepoService.findAll();
            } else {
                List<StDepoYetki> yetkiler = stDepoYetkiService.findByKullanici(kullanici);
                yetkiliDepolar = yetkiler == null ? List.of()
                        : yetkiler.stream().map(StDepoYetki::getStDepo)
                        .filter(Objects::nonNull).distinct().toList();
            }
        }
        return yetkiliDepolar;
    }

    public List<SelectItem> getYetkiliDepoOptions() {
        List<SelectItem> options = new ArrayList<>();
        for (StDepo depo : getYetkiliDepolar()) {
            options.add(new SelectItem(depo, depo.getAd()));
        }
        return options;
    }

    private Set<Integer> yetkiliDepoIds() {
        return getYetkiliDepolar().stream()
                .map(StDepo::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public StAlis prepareCreate(ActionEvent event) {
        StAlis yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setTarih(LocalDateTime.now());
            yeni.setKalemler(new ArrayList<>());
            if (sonDepo != null && yetkiliDepoIds().contains(sonDepo.getId())) {
                yeni.setStDepo(sonDepo);
            }
            yeni.setStTedarikci(sonTedarikci);
        }
        kalem = new StAlisKalem();
        barkodOku = null;
        alisMiktar = BigDecimal.ONE;
        alisBirimMaliyet = null;
        barkodBulunamadi = false;
        return yeni;
    }

    /**
     * Barkod veya stok kodu ile stok bulur.
     */
    public StStok stokBul(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String deger = value.trim();
        var barkodKaydi = stStokBarkodService.findByBarkod(deger);
        if (barkodKaydi != null && barkodKaydi.getStStok() != null) {
            return barkodKaydi.getStStok();
        }
        return stStokService.findByKod(deger);
    }

    /**
     * Hizli giris: barkod okutulunca tek harekette kalem ekler.
     */
    public void addKalemFromBarkod() {
        if (getSelected() == null) {
            return;
        }
        String deger = barkodOku == null ? "" : barkodOku.trim();
        if (deger.isEmpty()) {
            FacesUtil.addExclamationMessage("Barkod / Stok Kodu veya Malzeme Kodu girin");
            return;
        }
        StStok stok = stokBul(deger);
        if (stok == null) {
            barkodBulunamadi = true;
            FacesUtil.addExclamationMessage("Bu barkoda kayıtlı stok bulunamadı. Yeni stok kartı oluşturabilirsiniz.");
            return;
        }
        barkodBulunamadi = false;
        BigDecimal miktar = alisMiktar == null ? BigDecimal.ONE : alisMiktar;
        if (miktar.compareTo(BigDecimal.ZERO) <= 0) {
            FacesUtil.addExclamationMessage("Alış kalemi sıfırdan büyük olmalı");
            return;
        }
        StAlisKalem yeniKalem = new StAlisKalem();
        yeniKalem.setStStok(stok);
        yeniKalem.setMiktar(miktar);
        yeniKalem.setBirimMaliyet(alisBirimMaliyet);
        yeniKalem.setStAlis(getSelected());
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new ArrayList<>());
        }
        getSelected().getKalemler().add(yeniKalem);
        barkodOku = null;
        alisMiktar = BigDecimal.ONE;
        alisBirimMaliyet = null;
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    /**
     * Hucre duzenleme sonrasinda degerleri dogrular.
     * Miktar sifirdan kucuk/eksik girilirse onceki degeri geri yukler.
     */
    public void onCellEdit(CellEditEvent<BigDecimal> event) {
        if (event == null) {
            return;
        }
        String field = event.getColumn() == null ? null : event.getColumn().getField();
        if (!"miktar".equals(field)) {
            return;
        }
        Object rowData = event.getRowData();
        if (!(rowData instanceof StAlisKalem item)) {
            return;
        }
        if (item.getMiktar() == null || item.getMiktar().compareTo(BigDecimal.ZERO) <= 0) {
            Object oldValue = event.getOldValue();
            if (oldValue instanceof BigDecimal old) {
                item.setMiktar(old);
            }
            FacesContext.getCurrentInstance().validationFailed();
            FacesUtil.addExclamationMessage("Alış kalemi sıfırdan büyük olmalı");
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
            FacesUtil.addExclamationMessage("Alış kalemi sıfırdan büyük olmalı");
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

    public BigDecimal getKalemTutar(StAlisKalem item) {
        if (item == null || item.getMiktar() == null || item.getBirimMaliyet() == null) {
            return BigDecimal.ZERO;
        }
        return item.getMiktar().multiply(item.getBirimMaliyet());
    }

    public BigDecimal getToplamTutar() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return BigDecimal.ZERO;
        }
        return getSelected().getKalemler().stream()
                .filter(k -> k.getMiktar() != null && k.getBirimMaliyet() != null)
                .map(k -> k.getMiktar().multiply(k.getBirimMaliyet()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getToplamMiktar() {
        if (getSelected() == null || getSelected().getKalemler() == null) {
            return BigDecimal.ZERO;
        }
        return getSelected().getKalemler().stream()
                .filter(k -> k.getMiktar() != null)
                .map(StAlisKalem::getMiktar)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Barkod bulunamadiginda stok karti dialog'unu acar.
     */
    public void yeniStokKartiAc() {
        String deger = barkodOku == null ? "" : barkodOku.trim();
        stStokController.prepareCreate(null);
        if (!deger.isEmpty()) {
            stStokController.getBarkod().setBarkod(deger);
        }
        PrimeFaces.current().ajax().update(":StStokCreateForm");
        PrimeFaces.current().executeScript("PF('StStokCreateDialog').show()");
    }

    /**
     * Yeni olusturulan stok kartini alis kalemine aktarir.
     */
    public void sonStokuKalemeAl() {
        StStok stok = stStokController.getSelected();
        if (stok == null || stok.getId() == null) {
            FacesUtil.addExclamationMessage("Önce yeni stok kartını kaydedin");
            return;
        }
        barkodOku = stok.getStokKodu();
        barkodBulunamadi = false;
        addKalemFromBarkod();
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StAlis kayit = alisService.alisOlustur(getSelected(), this.getSyKullanici());
            setSelected(kayit);
            sonDepo = kayit.getStDepo();
            sonTedarikci = kayit.getStTedarikci();
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

    /** Belgeyi kaydeder ve ayni depo/tedarikci ile yeni belge acar. */
//    public void saveProceed(ActionEvent event) {
//        saveNew(event);
//        if (isValidationFailed()) {
//            return;
//        }
//        prepareCreate(event);
//    }

    /**
     * Listeden secilen belgeyi guncelleme icin hazirlar.
     */
    public void prepareEdit() {
        if (getSelected() == null || getSelected().getId() == null) {
            FacesUtil.addExclamationMessage("Güncellenecek alış belgesini seçin");
            return;
        }
        StAlis loaded = alisService.find(getSelected().getId());
        if (loaded == null) {
            FacesUtil.addExclamationMessage("Alış belgesi bulunamadı");
            return;
        }
        loaded.getKalemler().size();
        setSelected(loaded);
        barkodOku = null;
        alisMiktar = BigDecimal.ONE;
        alisBirimMaliyet = null;
        barkodBulunamadi = false;
    }

    /**
     * Guncellenen belgeyi kaydeder.
     */
    public void saveEdit(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StAlis kayit = alisService.updatePurchase(getSelected(), getSyKullanici());
            setSelected(kayit);
            sonDepo = kayit.getStDepo();
            sonTedarikci = kayit.getStTedarikci();
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Alis guncellenemedi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}


