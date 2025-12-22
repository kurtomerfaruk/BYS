package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FlowEvent;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlJoinTipi;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRapor;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporEntityBaglanti;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporKolon;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporParametre;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporService;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 12:50
 */
@Named
@ViewScoped
public class RaporYonetimController implements Serializable {

    @Serial
    private static final long serialVersionUID = -5496802307247223889L;
    @Inject
    private GnlRaporService raporYonetimService;

    // Ana liste
    private List<GnlRapor> modulListesi;

    // Yeni rapor modülü için
    private GnlRapor yeniRaporModul = new GnlRapor();
    private List<GnlRaporKolon> yeniKolonlar = new ArrayList<>();
    private List<GnlRaporParametre> yeniParametreler = new ArrayList<>();
    private List<GnlRaporEntityBaglanti> yeniEntityBaglantilari = new ArrayList<>();

    // UI için geçici nesneler
    private GnlRaporKolon yeniKolon = new GnlRaporKolon();
    private GnlRaporParametre yeniParametre = new GnlRaporParametre();
    private GnlRaporEntityBaglanti yeniEntityBaglanti = new GnlRaporEntityBaglanti();

    // Seçimler
    private List<String> seciliEntityler = new ArrayList<>();

    private boolean skip = false;

    @PostConstruct
    public void init() {
        modulListesi = raporYonetimService.findAll();
        yeniKolonlar = new ArrayList<>();
        yeniParametreler = new ArrayList<>();
        yeniEntityBaglantilari = new ArrayList<>();
    }

    public String onWizardFlow(FlowEvent event) {
        String currentStep = event.getOldStep();

        // Eğer skip true ise herhangi bir validation yapma
        if (skip) {
            skip = false;
            return event.getNewStep();
        }

        // Validasyonlar
        if ("1".equals(currentStep)) { // Temel Bilgiler -> Kolonlar
            boolean valid = validateStep1();
            if (!valid) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Temel bilgileri doldurun!", "Temel bilgileri doldurun!"));
                return currentStep;
            }
        }
        else if ("2".equals(currentStep)) { // Kolonlar -> Parametreler
            if (yeniKolonlar.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "En az bir kolon tanımlamalısınız!", "En az bir kolon tanımlamalısınız!"));
                return currentStep;
            }
        }

        return event.getNewStep();
    }

    private boolean validateStep1() {
        if (yeniRaporModul.getAd() == null || yeniRaporModul.getAd().trim().isEmpty()) {
            return false;
        }

        if (yeniRaporModul.getAnaEntity() == null || yeniRaporModul.getAnaEntity().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * SKIP İÇİN GETTER/SETTER
     */
    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public boolean isSkip() {
        return skip;
    }

    /**
     * YENİ RAPOR MODÜLÜ OLUŞTUR
     */
    public void yeniRaporModulKaydet() {
        try {
            // 1. Modülü kaydet
            yeniRaporModul.setGnlRaporKolonList(yeniKolonlar);
            yeniRaporModul.setGnlRaporParametreList(yeniParametreler);
            yeniRaporModul.setGnlRaporEntityBaglantiList(yeniEntityBaglantilari);
            raporYonetimService.create(yeniRaporModul);
            GnlRapor kaydedilen = yeniRaporModul;

            // 2. Kolonları kaydet
            for (GnlRaporKolon kolon : yeniKolonlar) {
                kolon.setGnlRapor(kaydedilen);
                //raporYonetimService.raporKolonKaydet(kolon);
            }

            // 3. Parametreleri kaydet
            for (GnlRaporParametre parametre : yeniParametreler) {
                parametre.setGnlRapor(kaydedilen);
                //raporYonetimService.raporParametreKaydet(parametre);
            }

            // 4. Entity bağlantılarını kaydet
            for (GnlRaporEntityBaglanti baglanti : yeniEntityBaglantilari) {
                baglanti.setGnlRapor(kaydedilen);
                //raporYonetimService.entityBaglantiKaydet(baglanti);
            }

            // Başarı mesajı
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Başarılı",
                            "Rapor modülü başarıyla kaydedildi!"));

            // Temizle
            temizle();

            // Listeyi güncelle
            modulListesi = raporYonetimService.findAll();

        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Hata",
                            "Kayıt sırasında hata oluştu: " + e.getMessage()));
        }
    }

    /**
     * YENİ KOLON EKLE
     */
    public void yeniKolonEkle() {
        if (yeniKolon.getAlanAdi() == null || yeniKolon.getAlanAdi().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Alan adı gereklidir!",
                            "Alan adı gereklidir!"));
            return;
        }

        // Varsayılan değerler
        if (yeniKolon.getGorunurAdi() == null || yeniKolon.getGorunurAdi().isEmpty()) {
            yeniKolon.setGorunurAdi(yeniKolon.getAlanAdi());
        }

        if (yeniKolon.getVeriTipi() == null) {
            //yeniKolon.setVeriTipi("STRING");
        }

        yeniKolon.setSiralama(yeniKolonlar.size() + 1);
        yeniKolon.setAktif(true);

        yeniKolonlar.add(yeniKolon);
        yeniKolon = new GnlRaporKolon(); // Yeni için temizle

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Başarılı",
                        "Kolon eklendi!"));
    }

    /**
     * KOLON SİL
     */
    public void kolonSil(GnlRaporKolon kolon) {
        yeniKolonlar.remove(kolon);
    }

    /**
     * YENİ PARAMETRE EKLE
     */
    public void yeniParametreEkle() {
        if (yeniParametre.getParametreAdi() == null || yeniParametre.getParametreAdi().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Parametre adı gereklidir!",
                            "Parametre adı gereklidir!"));
            return;
        }

        // Varsayılan değerler
        if (yeniParametre.getGorunurAdi() == null || yeniParametre.getGorunurAdi().isEmpty()) {
            yeniParametre.setGorunurAdi(yeniParametre.getParametreAdi());
        }

        if (yeniParametre.getVeriTipi() == null) {
            yeniParametre.setVeriTipi(EnumSyVeriTipi.STRING);
        }

        yeniParametre.setSiralama(yeniParametreler.size() + 1);
        yeniParametre.setAktif(true);

        yeniParametreler.add(yeniParametre);
        yeniParametre = new GnlRaporParametre(); // Yeni için temizle

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Başarılı",
                        "Parametre eklendi!"));
    }

    /**
     * PARAMETRE SİL
     */
    public void parametreSil(GnlRaporParametre parametre) {
        yeniParametreler.remove(parametre);
    }

    /**
     * YENİ ENTITY BAĞLANTISI EKLE
     */
    public void yeniEntityBaglantiEkle() {
        if (yeniEntityBaglanti.getEntityClass() == null || yeniEntityBaglanti.getEntityClass().trim().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Entity class gereklidir!",
                            "Entity class gereklidir!"));
            return;
        }

        // Entity adını class'tan çıkar
        if (yeniEntityBaglanti.getEntityAdi() == null || yeniEntityBaglanti.getEntityAdi().isEmpty()) {
            String[] parts = yeniEntityBaglanti.getEntityClass().split("\\.");
            yeniEntityBaglanti.setEntityAdi(parts[parts.length - 1]);
        }

        yeniEntityBaglanti.setSiralama(yeniEntityBaglantilari.size() + 1);

        yeniEntityBaglantilari.add(yeniEntityBaglanti);
        yeniEntityBaglanti = new GnlRaporEntityBaglanti(); // Yeni için temizle

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Başarılı",
                        "Entity bağlantısı eklendi!"));
    }

    /**
     * ENTITY BAĞLANTISI SİL
     */
    public void entityBaglantiSil(GnlRaporEntityBaglanti baglanti) {
        yeniEntityBaglantilari.remove(baglanti);
    }

    /**
     * TEMİZLE
     */
    public void temizle() {
        yeniRaporModul = new GnlRapor();
        yeniKolonlar = new ArrayList<>();
        yeniParametreler = new ArrayList<>();
        yeniEntityBaglantilari = new ArrayList<>();
        yeniKolon = new GnlRaporKolon();
        yeniParametre = new GnlRaporParametre();
        yeniEntityBaglanti = new GnlRaporEntityBaglanti();
    }

    /**
     * VERİ TİPLERİ (Dropdown için)
     */
    public List<String> getVeriTipleri() {
        return List.of("STRING", "INTEGER", "LONG", "DOUBLE", "BOOLEAN",
                "DATE", "DATETIME", "DECIMAL", "ENUM");
    }

    /**
     * JOIN TİPLERİ (Dropdown için)
     */
    public List<EnumGnlJoinTipi> getJoinTipleri() {
        return List.of(EnumGnlJoinTipi.values());
    }

    /**
     * PARAMETRE TİPLERİ (Dropdown için)
     */
    public List<String> getParametreTipleri() {
        return List.of("TEXT", "DATE", "SELECT", "MULTI_SELECT",
                "BOOLEAN", "NUMBER", "DATE_RANGE");
    }

    // GETTER - SETTER
    public List<GnlRapor> getModulListesi() { return modulListesi; }
    public void setModulListesi(List<GnlRapor> modulListesi) { this.modulListesi = modulListesi; }

    public GnlRapor getYeniRaporModul() { return yeniRaporModul; }
    public void setYeniRaporModul(GnlRapor yeniRaporModul) { this.yeniRaporModul = yeniRaporModul; }

    public List<GnlRaporKolon> getYeniKolonlar() { return yeniKolonlar; }
    public void setYeniKolonlar(List<GnlRaporKolon> yeniKolonlar) { this.yeniKolonlar = yeniKolonlar; }

    public List<GnlRaporParametre> getYeniParametreler() { return yeniParametreler; }
    public void setYeniParametreler(List<GnlRaporParametre> yeniParametreler) { this.yeniParametreler = yeniParametreler; }

    public List<GnlRaporEntityBaglanti> getYeniEntityBaglantilari() { return yeniEntityBaglantilari; }
    public void setYeniEntityBaglantilari(List<GnlRaporEntityBaglanti> yeniEntityBaglantilari) { this.yeniEntityBaglantilari = yeniEntityBaglantilari; }

    public GnlRaporKolon getYeniKolon() { return yeniKolon; }
    public void setYeniKolon(GnlRaporKolon yeniKolon) { this.yeniKolon = yeniKolon; }

    public GnlRaporParametre getYeniParametre() { return yeniParametre; }
    public void setYeniParametre(GnlRaporParametre yeniParametre) { this.yeniParametre = yeniParametre; }

    public GnlRaporEntityBaglanti getYeniEntityBaglanti() { return yeniEntityBaglanti; }
    public void setYeniEntityBaglanti(GnlRaporEntityBaglanti yeniEntityBaglanti) { this.yeniEntityBaglanti = yeniEntityBaglanti; }

    public List<String> getSeciliEntityler() { return seciliEntityler; }
    public void setSeciliEntityler(List<String> seciliEntityler) { this.seciliEntityler = seciliEntityler; }
}
