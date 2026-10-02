package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkSoruTanimService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkSoruTurService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 10:31
 */
@Named
@ViewScoped
@Slf4j
public class PkRevirAnemnezController  extends AbstractController<PkRevirAnemnez> {

    @Serial
    private static final long serialVersionUID = -4187758809562608138L;

    @Inject
    private PkSoruTurService soruTurService;

    @Inject
    private PkSoruTanimService soruTanimService;

    /**
     * Formun baglandigi soru gruplari. Bunlar kalici entity DEGIL, sadece
     * ekran durumu tutan gecici nesnelerdir; kaydetmede secimler
     * PkRevirAnemnezDetay satirlarina cevrilir.
     */
    private List<DetayGrup> detayGruplari = new ArrayList<>();

    /**
     * detayGruplari hangi kayit icin kuruldu? JSF formu gonderip ayni kaydi
     * yeniden render ettiginde grup listesi yeniden kurulmaz; aksi halde
     * kullanici secimleri render sirasinda ezilirdi.
     */
    private PkRevirAnemnez detayGrubuKaynagi;

    /**
     * Secim diyaloglarinda gosterilen salt-okunur metinler.
     * JSF bunlari form gonderiminde geri yazdigi icin yazilabilir olmalidir;
     * deger kayit degistiginde ilk cagrida varliktan yeniden hesaplanir.
     */
    @Getter
    @Setter
    private String gorusmeYapanAdSoyad;

    @Getter
    @Setter
    private String tedaviSekliHastaAdSoyad;

    private PkRevirAnemnez adSoyadKaynagi;

    public PkRevirAnemnezController() {
        super(PkRevirAnemnez.class);
    }

    @PostConstruct
    @Override
    public void init() {
        readColumns();
    }

    @Override
    public PkRevirAnemnez prepareCreate(ActionEvent event) {
        PkRevirAnemnez yeniKayit = super.prepareCreate(event);
        if (yeniKayit != null) {
            yeniKayit.setYatisTarihi(LocalDateTime.now());
            PkHastaTedaviSekli pkHastaTedaviSekli = new PkHastaTedaviSekli();
            pkHastaTedaviSekli.setPkHasta(PkHasta.builder().gnlKisi(new GnlKisi()).build());
            yeniKayit.setPkHastaTedaviSekli(pkHastaTedaviSekli);
            yeniKayit.setSonAltiAydaKiloKaybiVarMi(EnumVarYok.YOK);
            // Kayit yeni oldugu icin secili soru yoktur; detay satirlari
            // yalnizca kullanici bir secenek isaretlediginde olusur.
            yeniKayit.setPkRevirAnemnezDetayList(new ArrayList<>());
            detayGrubuKaynagi = null;
        }
        return yeniKayit;
    }

    /**
     * Formdaki soru gruplari ve secenekleri.
     *
     * Katalog (soru turu/tanim) salt okunur veridir; yalnizca "secildi" ve
     * "aciklama" bilgisi ekran durumudur.
     */
    public List<DetayGrup> getDetayGruplari() {
        PkRevirAnemnez kayit = getSelected();
        if (kayit == null) {
            return new ArrayList<>();
        }
        if (detayGrubuKaynagi != kayit) {
            detayGrubuKaynagi = kayit;
            detayGruplari = detayGruplariKur(kayit);
        }
        return detayGruplari;
    }

    private List<DetayGrup> detayGruplariKur(PkRevirAnemnez kayit) {
        List<PkSoruTur> turlar = soruTurService.findAktifler();
        List<PkSoruTanim> tanimlar = soruTanimService.findAktifler();

        Map<Integer, List<PkSoruTanim>> turIdyeGore = tanimlar.stream()
                .filter(tanim -> tanim.getPkSoruTur() != null
                        && tanim.getPkSoruTur().getId() != null)
                .collect(Collectors.groupingBy(tanim -> tanim.getPkSoruTur().getId()));

        // Kaydin daha once kaydedilmis secimleri: soru tanim id -> detay satiri
        Map<Integer, PkRevirAnemnezDetay> secilmisler = new HashMap<>();
        if (kayit.getPkRevirAnemnezDetayList() != null) {
            for (PkRevirAnemnezDetay detay : kayit.getPkRevirAnemnezDetayList()) {
                if (detay.getPkSoruTanim() != null
                        && detay.getPkSoruTanim().getId() != null) {
                    secilmisler.put(detay.getPkSoruTanim().getId(), detay);
                }
            }
        }

        List<DetayGrup> gruplar = new ArrayList<>();
        for (PkSoruTur tur : turlar) {
            List<PkSoruTanim> turSecenekleri = turIdyeGore.get(tur.getId());
            if (turSecenekleri == null || turSecenekleri.isEmpty()) {
                continue;
            }
            List<DetaySecenek> secenekler = new ArrayList<>();
            for (PkSoruTanim tanim : turSecenekleri) {
                secenekler.add(new DetaySecenek(tanim, secilmisler.get(tanim.getId())));
            }
            gruplar.add(new DetayGrup(tur, secenekler));
        }
        log.debug("Revir anemnez formu icin {} soru grubu / {} secenek yuklendi",
                gruplar.size(), tanimlar.size());
        return gruplar;
    }

    /**
     * Kaydetmeden once ekrandaki secimleri detay satirlarina yansitir.
     *
     * - isaretlenmis secenek icin detay satiri yoksa olusturulur
     * - isareti kaldirilan secenegin satiri koleksiyondan cikarilir
     *   (orphanRemoval sayesinde veritabaninda silinir)
     * - aciklama gerektirmeyen seceneklerde ve secim kaldirildiginda
     *   aciklama metni temizlenir
     */
    private void detaylariSenkronizeEt(PkRevirAnemnez kayit) {
        if (kayit.getPkRevirAnemnezDetayList() == null) {
            kayit.setPkRevirAnemnezDetayList(new ArrayList<>());
        }
        for (DetayGrup grup : getDetayGruplari()) {
            for (DetaySecenek secenek : grup.getSecenekler()) {
                boolean secili = grup.isCokluSecim()
                        ? secenek.isSecili()
                        : Objects.equals(grup.getSeciliTanimId(), secenek.getTanimId());
                if (secili) {
                    secenek.secimiKaydet(kayit);
                } else {
                    secenek.secimiKaldir(kayit);
                }
            }
        }
    }

    /**
     * Aciklamasi zorunlu secenekler icin dogrulama.
     */
    private boolean validateAciklama() {
        for (DetayGrup grup : getDetayGruplari()) {
            for (DetaySecenek secenek : grup.getSecenekler()) {
                boolean secili = grup.isCokluSecim()
                        ? secenek.isSecili()
                        : Objects.equals(grup.getSeciliTanimId(), secenek.getTanimId());
                if (secili && secenek.isAciklamaGerekli() && StringUtil.isBlank(secenek.getDigerAciklama())) {
                    FacesUtil.addErrorMessage(FacesUtil.message("revirSecilenSecenekAciklamaZorunlu"));
                    return false;
                }
            }
        }
        return true;
    }

    public void secilenPersonel(SelectEvent<PkPersonel> event) {
        if (event != null && event.getObject() != null && getSelected() != null) {
            getSelected().setGorusmeYapanPkPersonel(event.getObject());
            gorusmeYapanAdSoyad = personelinAdSoyadi(event.getObject());
        }
    }

    public void secilenTedaviSekli(SelectEvent<PkHastaTedaviSekli> event) {
        if (event != null && event.getObject() != null && getSelected() != null) {
            getSelected().setPkHastaTedaviSekli(event.getObject());
            if (getSelected().getYatisTarihi() == null && event.getObject().getTarih() != null) {
                getSelected().setYatisTarihi(event.getObject().getTarih());
            }
            tedaviSekliHastaAdSoyad = hastaninAdSoyadi(event.getObject().getPkHasta());
        }
    }

    /**
     * Edit dialogu her acildiginda secim kutularinin dogru metni gostermesi icin.
     * JSF formu gonderip yeniden render ettiginde degerler korunur; sadece
     * farkli bir kayit secildiginde yeniden hesaplanir.
     */
    public String getGorusmeYapanAdSoyad() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null) {
            return null;
        }
        if (naranon != adSoyadKaynagi) {
            adSoyadKaynagi = naranon;
            gorusmeYapanAdSoyad = personelinAdSoyadi(naranon.getGorusmeYapanPkPersonel());
            tedaviSekliHastaAdSoyad = tedaviSekliHastaninAdSoyadi(naranon.getPkHastaTedaviSekli());
        }
        return gorusmeYapanAdSoyad;
    }

    public String getTedaviSekliHastaAdSoyad() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null) {
            return null;
        }
        if (naranon != adSoyadKaynagi) {
            getGorusmeYapanAdSoyad();
        }
        return tedaviSekliHastaAdSoyad;
    }

    private String personelinAdSoyadi(PkPersonel personel) {
        if (personel == null || personel.getGnlPersonel() == null || personel.getGnlPersonel().getGnlKisi() == null) {
            return null;
        }
        return personel.getGnlPersonel().getGnlKisi().getAdSoyad();
    }

    private String tedaviSekliHastaninAdSoyadi(PkHastaTedaviSekli tedaviSekli) {
        if (tedaviSekli == null) {
            return null;
        }
        return hastaninAdSoyadi(tedaviSekli.getPkHasta());
    }

    private String hastaninAdSoyadi(PkHasta hasta) {
        if (hasta == null || hasta.getGnlKisi() == null) {
            return null;
        }
        return hasta.getGnlKisi().getAdSoyad();
    }

    private boolean validateKayit() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("kayitBulunamadi"));
            return false;
        }
        if (naranon.getYatisTarihi() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirYatisTarihiSecmelisiniz"));
            return false;
        }
        if (naranon.getPkHastaTedaviSekli() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirHastaSecmelisiniz"));
            return false;
        }
        if (naranon.getGorusmeYapanPkPersonel() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirPersonelSecmelisiniz"));
            return false;
        }
        if (StringUtil.isBlank(naranon.getProtokolNo())) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirProtokolNoZorunlu"));
            return false;
        }
        return true;
    }

    @Override
    public void save(ActionEvent event) {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null || !validateKayit() || !validateAciklama()) {
            return;
        }
        detaylariSenkronizeEt(naranon);
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null || !validateKayit() || !validateAciklama()) {
            return;
        }
        detaylariSenkronizeEt(naranon);
        super.saveNew(event);
    }

    /**
     * Bir soru grubu ve o grubun secenekleri.
     */
    @Getter
    public static class DetayGrup implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 8510041304742188221L;

        /** Katalog kaydi; salt okunur. */
        private final PkSoruTur tur;

        private final List<DetaySecenek> secenekler;

        /**
         * Tek secimli gruplarda secilen secenegin tanim id'si.
         * ALERJISI gibi gruplarda checkbox yerine radio kullanilir ve
         * deger buraya baglanir. Hicbiri secilmemis olabilir (null).
         */
        @Getter
        @Setter
        private Integer seciliTanimId;

        DetayGrup(PkSoruTur tur, List<DetaySecenek> secenekler) {
            this.tur = tur;
            this.secenekler = secenekler;
            if (!tur.isCokluSecim()) {
                this.seciliTanimId = secenekler.stream()
                        .filter(DetaySecenek::isSecili)
                        .map(DetaySecenek::getTanimId)
                        .findFirst()
                        .orElse(null);
            }
        }

        public String getTurKod() {
            return "tur-kod";
        }

        public String getTurAdi() {
            return tur.getTanim();
        }

        public boolean isCokluSecim() {
            return tur.isCokluSecim();
        }

        /**
         * Gruptan en az bir secenek secilip secilmedigi (salt okunur ekranda
         * "secim yok" yazisi gostermek icin).
         */
        public boolean isSeciliVar() {
            return secenekler.stream().anyMatch(DetaySecenek::isSecili);
        }

        /**
         * Salt okunur ekranda yalnizca secilenler gosterilir.
         */
        public List<DetaySecenek> getSeciliSecenekler() {
            return secenekler.stream().filter(DetaySecenek::isSecili).collect(Collectors.toList());
        }
    }

    /**
     * Bir soru secenegi. Katalog kaydi (tanim) salt okunur;
     * secili/digerAciklama ekran durumudur; detay ise kayit varsa
     * veritabanindaki satirdir (yoksa kayit aninda olusur).
     */
    @Getter
    @Setter
    public static class DetaySecenek implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 3274108853102647218L;

        /** Katalog kaydi; salt okunur. */
        private final PkSoruTanim tanim;

        private boolean secili;

        private String digerAciklama;

        private PkRevirAnemnezDetay detay;

        DetaySecenek(PkSoruTanim tanim, PkRevirAnemnezDetay detay) {
            this.tanim = tanim;
            this.detay = detay;
            this.secili = detay != null;
            this.digerAciklama = detay != null ? detay.getDigerAciklama() : null;
        }

        public Integer getTanimId() {
            return tanim.getId();
        }

        public String getKod() {
            return "kod";
        }

        public String getAdi() {
            return tanim.getTanim();
        }

        public boolean isAciklamaGerekli() {
            return tanim.isAciklamaGerekli();
        }

        /**
         * Secenek isaretli: detay satiri yoksa olustur, varsa guncelle.
         */
        void secimiKaydet(PkRevirAnemnez kayit) {
            if (detay == null) {
                detay = new PkRevirAnemnezDetay();
                detay.setPkRevirAnemnez(kayit);
                detay.setPkSoruTanim(tanim);
                kayit.getPkRevirAnemnezDetayList().add(detay);
            }
            if (!tanim.isAciklamaGerekli()) {
                digerAciklama = null;
            }
            detay.setDigerAciklama(StringUtil.isBlank(digerAciklama) ? null : digerAciklama);
        }

        /**
         * Secenek isareti kaldirildi: detay satiri silinir, aciklama temizlenir.
         * Boylece daha once yazilmis "diger" metni sonraki acilista geri gelmez.
         */
        void secimiKaldir(PkRevirAnemnez kayit) {
            if (detay != null) {
                kayit.getPkRevirAnemnezDetayList().remove(detay);
                detay = null;
            }
            digerAciklama = null;
        }
    }
}