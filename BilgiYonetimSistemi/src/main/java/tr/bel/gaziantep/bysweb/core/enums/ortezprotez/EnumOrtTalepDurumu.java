package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 15:36
 */
@Getter
public enum EnumOrtTalepDurumu implements BaseEnum {

    YENI_TALEP("Yeni Talep"),
    ON_MUAYENE_RANDEVUSU_BEKLIYOR("Ön Muayene Randevusu Bekliyor"),
    ON_MUAYENEYE_GELDI("Ön Muayeneye Geldi"),
    PERSONEL_ATAMASI_BEKLIYOR("Personel Ataması Bekliyor"),
    DEGERLENDIRME_BEKLIYOR("Değerlendirme Bekliyor"),
    TALEP_REDDEDILDI("Talep Reddedildi"),
    UCRET_BELIRLEME_BEKLIYOR("Ücret Belirleme Bekliyor"),
    SOZLESME_BEKLIYOR("Sözleşme Bekliyor"),
    ODEME_BEKLIYOR("Ödeme Bekliyor"),
    PROJE_BELIRLENDI("Proje Belirlendi"),
    OLCU_RANDEVUSU_BEKLIYOR("Ölçü Randevusu Bekliyor"),
    OLCU_ALINDI("Ölçü Alındı"),
    FORMLAR_BEKLENIYOR("Formlar Bekleniyor"),
    DOKTOR_RAPORU_BEKLENIYOR("Doktor Raporu Bekleniyor"),
    RECETE_BEKLENIYOR("Reçete Bekleniyor"),
    MALZEME_TALEBI_BEKLIYOR("Malzeme Talebi Bekliyor"),
    DEPO_ONAYI_BEKLIYOR("Depo Onayı Bekliyor"),
    PROVA_RANDEVUSU_BEKLIYOR("Prova Randevusu Bekliyor"),
    PROVA_YAPILDI("Prova Yapıldı"),
    YENIDEN_PROVA_BEKLIYOR("Yeniden Prova Bekliyor"),
    TESLIMAT_BEKLIYOR("Teslimat Bekliyor"),
    TESLIM_EDILDI("Teslim Edildi"),
    TAMAMLANDI("Tamamlandı"),
    KAPATILDI("Kapatıldı");

    private final String label;

    EnumOrtTalepDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
