package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumVarYok;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkRevirAnemnezTanim;
import tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi.EnumPkRevirAnemnezTur;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkRevirAnemnez;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkRevirAnemnezDetay;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Getter
    @Setter
    private String alerjiSecimi;

    private Integer alerjiSecimiKayitId;

    public PkRevirAnemnezController() {
        super(PkRevirAnemnez.class);
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
            if (yeniKayit.getPkRevirAnemnezDetayList() == null) {
                yeniKayit.setPkRevirAnemnezDetayList(new ArrayList<>());
            } else {
                yeniKayit.getPkRevirAnemnezDetayList().clear();
            }
            detaySatirlariniTamamla(yeniKayit);
            alerjiSecimiKayitId = yeniKayit.getId();
            alerjiSecimi = null;
        }
        return yeniKayit;
    }

    /**
     * Formdaki secim gruplari. Her grup kendi detay satirlarini tasir.
     */
    public List<DetayGrup> getDetayGruplari() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null) {
            return new ArrayList<>();
        }
        detaySatirlariniTamamla(naranon);
        List<DetayGrup> gruplar = new ArrayList<>();
        for (EnumPkRevirAnemnezTur tur : EnumPkRevirAnemnezTur.values()) {
            gruplar.add(new DetayGrup(tur, naranon.getPkRevirAnemnezDetayList()));
        }
        return gruplar;
    }

    /**
     * Enum'da tanimli her secenek icin detay satiri olusturur.
     * Kayitli kayitlarda eksik satirlari tamamlar (yeni secenek eklendiginde
     * eski kayitlarin da gosterilmemesi icin).
     */
    private void detaySatirlariniTamamla(PkRevirAnemnez naranon) {
        if (naranon == null) {
            return;
        }
        List<PkRevirAnemnezDetay> detaylar = naranon.getPkRevirAnemnezDetayList();
        if (detaylar == null) {
            detaylar = new ArrayList<>();
            naranon.setPkRevirAnemnezDetayList(detaylar);
        }
        for (EnumPkRevirAnemnezTur tur : EnumPkRevirAnemnezTur.values()) {
            for (EnumPkRevirAnemnezTanim tanim : tur.getSecenekler()) {
                boolean var = detaylar.stream()
                        .anyMatch(d -> tur.name().equals(d.getTur()) && tanim.name().equals(d.getTanim()));
                if (!var) {
                    PkRevirAnemnezDetay detay = new PkRevirAnemnezDetay();
                    detay.setPkRevirAnemnez(naranon);
                    detay.setTur(tur.name());
                    detay.setTanim(tanim.name());
                    detay.setSecili(false);
                    detaylar.add(detay);
                }
            }
        }
    }

    /**
     * Tek secimli ALERJISI grubunun secili secenegini dondurur.
     * Ilk cagrida detay satirlarindan okunur, sonraki cagrilarda
     * kullanicinin sectigi deger korunur.
     */
    public String getAlerjiSecimi() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null) {
            return null;
        }
        if (!Objects.equals(alerjiSecimiKayitId, naranon.getId())) {
            alerjiSecimiKayitId = naranon.getId();
            alerjiSecimi = naranon.getPkRevirAnemnezDetayList().stream()
                    .filter(d -> EnumPkRevirAnemnezTur.ALERJISI.name().equals(d.getTur()) && d.isSecili())
                    .map(PkRevirAnemnezDetay::getTanim)
                    .findFirst()
                    .orElse(null);
        }
        return alerjiSecimi;
    }

    /**
     * ALERJISI tek secim oldugu icin diger seceneklerin secili=false yapilir.
     */
    private void alerjiSeciminiUygula() {
        PkRevirAnemnez naranon = getSelected();
        if (naranon == null || naranon.getPkRevirAnemnezDetayList() == null) {
            return;
        }
        for (PkRevirAnemnezDetay detay : naranon.getPkRevirAnemnezDetayList()) {
            if (EnumPkRevirAnemnezTur.ALERJISI.name().equals(detay.getTur())) {
                detay.setSecili(Objects.equals(detay.getTanim(), alerjiSecimi));
            }
        }
    }

    public void secilenTedaviSekli(SelectEvent<PkHastaTedaviSekli> event) {
        PkHastaTedaviSekli pkHastaTedaviSekli = event.getObject();
        this.getSelected().setPkHastaTedaviSekli(pkHastaTedaviSekli);
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
        if (!validateKayit()) {
            return;
        }
        alerjiSeciminiUygula();
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (!validateKayit()) {
            return;
        }
        alerjiSeciminiUygula();
        super.saveNew(event);
    }

    /**
     * Bir secim grubu ve o grubun detay satirlari.
     * Satirlar dogrudan PkRevirAnemnezDetay entity'leridir; checkbox'lar
     * bu satirlarin secili alanina baglandigi icin kaydetmede senkronizasyon
     * gerekmez.
     */
    @Getter
    public static class DetayGrup implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 8510041304742188221L;

        private final EnumPkRevirAnemnezTur tur;
        private final List<PkRevirAnemnezDetay> satirlar;

        DetayGrup(EnumPkRevirAnemnezTur tur, List<PkRevirAnemnezDetay> detaylar) {
            this.tur = tur;
            this.satirlar = new ArrayList<>();
            for (EnumPkRevirAnemnezTanim tanim : tur.getSecenekler()) {
                for (PkRevirAnemnezDetay detay : detaylar) {
                    if (tur.name().equals(detay.getTur()) && tanim.name().equals(detay.getTanim())) {
                        this.satirlar.add(detay);
                        break;
                    }
                }
            }
        }

        public String getTurLabel() {
            return tur.getDisplayValue();
        }

        public boolean isCokluSecim() {
            return tur.isCokluSecim();
        }

        public String getTanimLabel(PkRevirAnemnezDetay detay) {
            try {
                return EnumPkRevirAnemnezTanim.valueOf(detay.getTanim()).getDisplayValue();
            } catch (IllegalArgumentException | NullPointerException ex) {
                return detay.getTanim();
            }
        }

        public boolean isAciklamaGerekli(PkRevirAnemnezDetay detay) {
            try {
                return EnumPkRevirAnemnezTanim.valueOf(detay.getTanim()).isAciklamaGerekli();
            } catch (IllegalArgumentException | NullPointerException ex) {
                return false;
            }
        }

        /**
         * Gruptan en az bir secenek secilip secilmedigi (salt okunur ekranda
         * "secim yok" yazisi gostermek icin).
         */
        public boolean isSeciliVar() {
            return satirlar.stream().anyMatch(PkRevirAnemnezDetay::isSecili);
        }
    }
}