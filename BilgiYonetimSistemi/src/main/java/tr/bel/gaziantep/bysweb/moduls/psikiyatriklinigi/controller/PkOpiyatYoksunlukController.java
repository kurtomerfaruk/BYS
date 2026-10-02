package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkModul;
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
 * @since 2.10.2026 11:44
 */
@Named
@ViewScoped
@Slf4j
public class PkOpiyatYoksunlukController extends AbstractController<PkOpiyatYoksunluk> {

    @Serial
    private static final long serialVersionUID = 8001152932636924143L;

    @Inject
    private PkSoruTurService soruTurService;
    @Inject
    private PkSoruTanimService soruTanimService;


    private List<DetayGrup> detayGruplari = new ArrayList<>();

    private PkOpiyatYoksunluk detayGrubuKaynagi;

    public PkOpiyatYoksunlukController() {
        super(PkOpiyatYoksunluk.class);
    }

    @Override
    public PkOpiyatYoksunluk prepareCreate(ActionEvent event) {
        PkOpiyatYoksunluk yeniKayit = super.prepareCreate(event);
        if (yeniKayit != null) {
            yeniKayit.setTarih(LocalDateTime.now());
            yeniKayit.setPkHasta(PkHasta.builder().gnlKisi(new GnlKisi()).build());
            yeniKayit.setToplamPuan(0);
            yeniKayit.setPkOpiyatYoksunlukDetayList(new ArrayList<>());
            detayGrubuKaynagi = null;
        }
        return yeniKayit;
    }

    public List<DetayGrup> getDetayGruplari() {
        PkOpiyatYoksunluk kayit = getSelected();
        if (kayit == null) {
            return new ArrayList<>();
        }
        if (detayGrubuKaynagi != kayit) {
            detayGrubuKaynagi = kayit;
            detayGruplari = detayGruplariKur(kayit);
        }
        return detayGruplari;
    }

    private List<DetayGrup> detayGruplariKur(PkOpiyatYoksunluk kayit) {
        List<PkSoruTur> turlar = soruTurService.findAktifler(EnumPkModul.OPIYAT);
        List<PkSoruTanim> tanimlar = soruTanimService.findAktifler(EnumPkModul.OPIYAT);

        Map<Integer, List<PkSoruTanim>> turIdyeGore = tanimlar.stream()
                .filter(tanim -> tanim.getPkSoruTur() != null
                        && tanim.getPkSoruTur().getId() != null)
                .collect(Collectors.groupingBy(tanim -> tanim.getPkSoruTur().getId()));

        Map<Integer, PkOpiyatYoksunlukDetay> kayitliCevaplar = new HashMap<>();
        if (kayit.getPkOpiyatYoksunlukDetayList() != null) {
            for (PkOpiyatYoksunlukDetay detay : kayit.getPkOpiyatYoksunlukDetayList()) {
                if (detay.getPkSoruTanim() != null
                        && detay.getPkSoruTanim().getId() != null) {
                    kayitliCevaplar.put(detay.getPkSoruTanim().getId(), detay);
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
                secenekler.add(new DetaySecenek(tanim, kayitliCevaplar.get(tanim.getId())));
            }
            gruplar.add(new DetayGrup(tur, secenekler));
        }
        log.debug("Revir anemnez formu icin {} soru grubu / {} secenek yuklendi",
                gruplar.size(), tanimlar.size());
        return gruplar;
    }

    private void detaylariSenkronizeEt(PkOpiyatYoksunluk kayit) {
        if (kayit.getPkOpiyatYoksunlukDetayList() == null) {
            kayit.setPkOpiyatYoksunlukDetayList(new ArrayList<>());
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

    public void secilenHasta(SelectEvent<PkHasta> event) {
        PkHasta pkHasta = event.getObject();
        this.getSelected().setPkHasta(pkHasta);
    }

    private boolean validateKayit() {
        PkOpiyatYoksunluk naranon = getSelected();
        if (naranon == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("kayitBulunamadi"));
            return false;
        }
        if (naranon.getTarih() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirYatisTarihiSecmelisiniz"));
            return false;
        }
        if (naranon.getPkHasta() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("revirHastaSecmelisiniz"));
            return false;
        }
        return true;
    }

    @Override
    public void save(ActionEvent event) {
        PkOpiyatYoksunluk naranon = getSelected();
        if (naranon == null || !validateKayit() || !validateAciklama()) {
            return;
        }
        detaylariSenkronizeEt(naranon);
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        PkOpiyatYoksunluk naranon = getSelected();
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

        /**
         * Detay satiri; null ise bu soru hic cevaplanmamis demektir.
         * AKTIF=false bir satir varsa kullanici sonra isareti kaldirmistir.
         */
        private PkOpiyatYoksunlukDetay detay;

        DetaySecenek(PkSoruTanim tanim, PkOpiyatYoksunlukDetay detay) {
            this.tanim = tanim;
            this.detay = detay;
            this.secili = detay != null && detay.isAktif();
            // Pasif satirin aciklamasi ekrana yuklenmez; kullanici soruyu
            // yeniden secerse bos alacaktir (eski metin veritabaninda kalir).
            this.digerAciklama = this.secili ? detay.getDigerAciklama() : null;
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
         * Secenek isaretli.
         *
         * Once cevaplanmis ama pasifleştirilmis bir soru yeniden secildiyse
         * yeni satir acmaz; mevcut satir yeniden aktiflestirilir. Aksi halde
         * UNIQUE (PKREVIR_ANEMNEZ_ID, PKREVIR_ANEMNEZ_SORU_TANIM_ID) kisiti
         * ihlal edilirdi.
         */
        void secimiKaydet(PkOpiyatYoksunluk kayit) {
            if (detay == null) {
                detay = new PkOpiyatYoksunlukDetay();
                detay.setPkOpiyatYoksunluk(kayit);
                detay.setPkSoruTanim(tanim);
                detay.setAktif(true);
                kayit.getPkOpiyatYoksunlukDetayList().add(detay);
            } else {
                detay.setAktif(true);
            }
            if (!tanim.isAciklamaGerekli()) {
                digerAciklama = null;
            }
            detay.setDigerAciklama(StringUtil.isBlank(digerAciklama) ? null : digerAciklama);
        }

        /**
         * Secenegin isareti kaldirildi: satir SILINMEZ, AKTIF=false yapilir.
         *
         * Boylece gecmis cevap ve degistiren kullanici/tarih bilgisi korunur.
         * Satir koleksiyonda kalir; soru yeniden secilirse ayni satir
         * yeniden aktiflestirilir. Eski aciklama metni de veritabaninda
         * durur, ama ekranda gosterilmez.
         */
        void secimiKaldir(PkOpiyatYoksunluk kayit) {
            if (detay != null) {
                detay.setAktif(false);
            }
            digerAciklama = null;
        }
    }
}
