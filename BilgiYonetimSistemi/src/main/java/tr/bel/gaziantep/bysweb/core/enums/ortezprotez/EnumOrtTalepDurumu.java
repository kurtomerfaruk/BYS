package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

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

    /**
     * Geri dönüşte hedef alınabilecek önceki durumlar. Kapatılmış talep geri dönüşü yok.
     * Dallanan akışlarda (ücretli/proje, prova/yeniden prova) birden fazla hedef olabilir.
     */
    private static final Map<EnumOrtTalepDurumu, Set<EnumOrtTalepDurumu>> PREVIOUS = Map.ofEntries(
            Map.entry(ON_MUAYENE_RANDEVUSU_BEKLIYOR, EnumSet.of(YENI_TALEP)),
            Map.entry(ON_MUAYENEYE_GELDI, EnumSet.of(ON_MUAYENE_RANDEVUSU_BEKLIYOR)),
            Map.entry(PERSONEL_ATAMASI_BEKLIYOR, EnumSet.of(ON_MUAYENEYE_GELDI)),
            Map.entry(DEGERLENDIRME_BEKLIYOR, EnumSet.of(PERSONEL_ATAMASI_BEKLIYOR)),
            Map.entry(TALEP_REDDEDILDI, EnumSet.of(DEGERLENDIRME_BEKLIYOR)),
            Map.entry(UCRET_BELIRLEME_BEKLIYOR, EnumSet.of(DEGERLENDIRME_BEKLIYOR)),
            Map.entry(SOZLESME_BEKLIYOR, EnumSet.of(UCRET_BELIRLEME_BEKLIYOR)),
            Map.entry(ODEME_BEKLIYOR, EnumSet.of(SOZLESME_BEKLIYOR)),
            Map.entry(PROJE_BELIRLENDI, EnumSet.of(DEGERLENDIRME_BEKLIYOR)),
            Map.entry(OLCU_RANDEVUSU_BEKLIYOR, EnumSet.of(PROJE_BELIRLENDI, ODEME_BEKLIYOR)),
            Map.entry(OLCU_ALINDI, EnumSet.of(OLCU_RANDEVUSU_BEKLIYOR)),
            Map.entry(FORMLAR_BEKLENIYOR, EnumSet.of(OLCU_ALINDI)),
            Map.entry(DOKTOR_RAPORU_BEKLENIYOR, EnumSet.of(FORMLAR_BEKLENIYOR)),
            Map.entry(RECETE_BEKLENIYOR, EnumSet.of(DOKTOR_RAPORU_BEKLENIYOR)),
            Map.entry(MALZEME_TALEBI_BEKLIYOR, EnumSet.of(RECETE_BEKLENIYOR)),
            Map.entry(DEPO_ONAYI_BEKLIYOR, EnumSet.of(MALZEME_TALEBI_BEKLIYOR)),
            Map.entry(PROVA_RANDEVUSU_BEKLIYOR, EnumSet.of(DEPO_ONAYI_BEKLIYOR, YENIDEN_PROVA_BEKLIYOR)),
            Map.entry(PROVA_YAPILDI, EnumSet.of(PROVA_RANDEVUSU_BEKLIYOR)),
            Map.entry(YENIDEN_PROVA_BEKLIYOR, EnumSet.of(PROVA_YAPILDI)),
            Map.entry(TESLIMAT_BEKLIYOR, EnumSet.of(PROVA_YAPILDI)),
            Map.entry(TESLIM_EDILDI, EnumSet.of(TESLIMAT_BEKLIYOR)),
            Map.entry(TAMAMLANDI, EnumSet.of(TESLIM_EDILDI))
    );

    EnumOrtTalepDurumu(String label) {
        this.label = label;
    }

    /**
     * Bu durumdan geri dönülebilecek durumlar.
     */
    public Set<EnumOrtTalepDurumu> getPreviousStatuses() {
        if (this == YENI_TALEP || this == KAPATILDI) {
            return EnumSet.noneOf(EnumOrtTalepDurumu.class);
        }
        return PREVIOUS.getOrDefault(this, EnumSet.noneOf(EnumOrtTalepDurumu.class));
    }

    /**
     * Geri dönüş yapılabilir mi?
     */
    public boolean canGoBack() {
        return !getPreviousStatuses().isEmpty();
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
