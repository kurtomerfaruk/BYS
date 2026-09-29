package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkAileService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkHastaService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 08:28
 */
@Named
@ViewScoped
@Slf4j
public class PkAileController extends AbstractController<PkAile> {

    @Serial
    private static final long serialVersionUID = -4271019910855765181L;

    @Inject
    private PkAileService service;
    @Inject
    private KpsController kpsController;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private PkHastaService pkHastaService;

    @Getter
    @Setter
    private PkHasta secilenHasta;

    public PkAileController() {
        super(PkAile.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case YAKINLIK_DERECESI -> {
                return filterOptionService.getGnlYakinlikDerecesis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public PkAile prepareCreate(ActionEvent event) {
        PkAile newItem;
        try {
            newItem = PkAile.class.getDeclaredConstructor().newInstance();
            newItem.setBasvuruTarihi(LocalDateTime.now());
            GnlKisi gnlKisi = GnlKisi.builder().gnlIlce(new GnlIlce()).gnlMahalle(new GnlMahalle()).build();
            newItem.setGnlKisi(gnlKisi);
            newItem.setPkHastaCocuklukDonemi(new PkHastaCocuklukDonemi());
            newItem.setPkHastaAileIciIliski(new PkHastaAileIciIliski());
            newItem.setPkHastaAdliSicil(new PkHastaAdliSicil());
            newItem.setPkHastaList(new ArrayList<>());
            this.setSelected(newItem);
            secilenHasta = null;
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
        if (this.getSelected() == null) return;
        try {
            String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();

            GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
            if (kisi == null) kisi = this.getSelected().getGnlKisi();
            kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.PSIKIYATRI_KLINIGI);
            this.getSelected().setGnlKisi(kisi);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Aile detayinda kullanilan hasta listesini dondurur. Liste null ise bos liste olusturulur.
     */
    public List<PkHasta> getHastaList() {
        PkAile pkAile = this.getSelected();
        if (pkAile == null) {
            return new ArrayList<>();
        }
        if (pkAile.getPkHastaList() == null) {
            pkAile.setPkHastaList(new ArrayList<>());
        }
        return pkAile.getPkHastaList();
    }

    /**
     * Kayitli hastalar arasindan secilen hastayi aileye ekler.
     * Hasta secme ekrani ortak diyalog (pkHastaSec) uzerinden acilir.
     */
    public void secilenHastaEkle(SelectEvent<PkHasta> event) {
        PkHasta secilen = event.getObject();
        PkAile pkAile = this.getSelected();
        if (secilen == null || pkAile == null) {
            return;
        }
        try {
            PkHasta kayitliHasta = pkHastaService.find(secilen.getId());
            if (kayitliHasta == null || !kayitliHasta.isAktif()) {
                FacesUtil.addErrorMessage("Secilen hasta kaydi bulunamadi veya pasif durumda.");
                return;
            }
            for (PkHasta pkHasta : getHastaList()) {
                if (Objects.equals(pkHasta.getId(), kayitliHasta.getId())) {
                    FacesUtil.addErrorMessage(hastaAdi(kayitliHasta) + " zaten aileye eklenmis.");
                    return;
                }
            }
            if (!service.aileyeEklenebilirMi(kayitliHasta, pkAile)) {
                FacesUtil.addErrorMessage(hastaAdi(kayitliHasta)
                        + " baska bir aileye bagli. Aile degistirmek icin once hastayi aileden cikariniz.");
                return;
            }
            getHastaList().add(kayitliHasta);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Hasta listesinden satiri cikarir. Kayitli hastalar pasif duruma alinir,
     * boylece aile detayinda ve hasta listelerinde gorunmez olurlar.
     */
    public void hastaSil(PkHasta pkHasta) {
        if (pkHasta == null) {
            return;
        }
        try {
            getHastaList().remove(pkHasta);
            if (pkHasta.getId() != null) {
                pkHasta.setPkAile(null);
                pkHastaService.edit(pkHasta);
            }
            if (Objects.equals(secilenHasta, pkHasta)) {
                secilenHasta = null;
            }
            FacesUtil.successMessage(Constants.KAYIT_SILINDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Satirdaki hastanin detay bilgilerini gostermek icin secilen hastayi set eder.
     */
    public void hastaDetay(PkHasta pkHasta) {
        this.secilenHasta = pkHasta;
    }

    @Override
    public void save(ActionEvent event) {
        kaydet(Constants.KAYIT_GUNCELLENDI);
    }

    @Override
    public void saveNew(ActionEvent event) {
        kaydet(Constants.KAYIT_EKLENDI);
    }

    /**
     * Aile kaydini, secilen kayitli hastalarla birlikte kaydeder.
     *
     * @param successMessage kayit sonrasi gosterilecek mesaj
     */
    private void kaydet(String successMessage) {
        PkAile pkAile = this.getSelected();
        if (pkAile == null) {
            return;
        }
        try {
            if (!validateHastaListesi()) {
                return;
            }
            setSelected(service.save(pkAile));
            setItems(null);
            setLazyItems(null);
            secilenHasta = null;
            FacesUtil.successMessage(successMessage);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.KAYIT_EKLENIRKEN_GUNCELLENIRKEN_HATA_OLUSTU);
        }
    }

    /**
     * Aileye bagli hasta listesinin gecerliligini kontrol eder.
     * Kayitli, aktif ve baska bir aileye bagli olmayan hastalar kabul edilir.
     *
     * @return liste gecerliyse true, degilse false
     */
    private boolean validateHastaListesi() {
        PkAile pkAile = this.getSelected();
        Set<Integer> idList = new HashSet<>();
        for (PkHasta pkHasta : getHastaList()) {
            if (pkHasta == null || pkHasta.getId() == null) {
                continue;
            }
            if (!idList.add(pkHasta.getId())) {
                FacesUtil.addErrorMessage("Aileye ait hasta listesinde ayni hasta birden fazla kez yer aliyor.");
                return false;
            }
            PkHasta kayitliHasta = pkHastaService.find(pkHasta.getId());
            if (kayitliHasta == null || !kayitliHasta.isAktif()) {
                FacesUtil.addErrorMessage("Hasta listesindeki kayit bulunamadi veya pasif durumda.");
                return false;
            }
            if (!service.aileyeEklenebilirMi(kayitliHasta, pkAile)) {
                FacesUtil.addErrorMessage(hastaAdi(kayitliHasta) + " baska bir aileye bagli.");
                return false;
            }
        }
        return true;
    }

    /**
     * Hata mesajlarinda kullanilacak hasta adini dondurur.
     */
    private String hastaAdi(PkHasta pkHasta) {
        GnlKisi gnlKisi = pkHasta.getGnlKisi();
        if (gnlKisi == null) {
            return "Seçilen hasta";
        }
        String adSoyad = ((gnlKisi.getAd() == null ? "" : gnlKisi.getAd())
                + " " + (gnlKisi.getSoyad() == null ? "" : gnlKisi.getSoyad())).trim();
        return adSoyad.isEmpty() ? "Seçilen hasta" : adSoyad;
    }

    public void aileSecKapat(PkAile pkAile) {
        PrimeFaces.current().dialog().closeDynamic(pkAile);
    }

    public void onRowDblSelect(SelectEvent<PkAile> event) {
        PkAile pkAile = event.getObject();
        aileSecKapat(pkAile);
    }
}
