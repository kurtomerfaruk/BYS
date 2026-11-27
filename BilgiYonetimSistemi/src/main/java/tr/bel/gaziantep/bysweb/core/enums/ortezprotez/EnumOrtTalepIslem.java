package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:44
 */
@Getter
public enum EnumOrtTalepIslem implements BaseEnum {

    ON_MUAYENE_RANDEVUSU("Ön Muayene Randevusu Oluştur", EnumOrtTalepDurumu.YENI_TALEP,
            EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR, "orttalep:guncelle", "pi pi-calendar-plus"),
    ON_MUAYENEYE_GEL("Ön Muayeneye Geldi", EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.ON_MUAYENEYE_GELDI, "orttalep:guncelle", "pi pi-check-circle"),
    PERSONEL_ATA("Personel Ataması Yap", EnumOrtTalepDurumu.ON_MUAYENEYE_GELDI,
            EnumOrtTalepDurumu.PERSONEL_ATAMASI_BEKLIYOR, "orttalep:guncelle", "pi pi-users"),
    DEGERLENDIRMEYE_AL("Değerlendirmeye Al", EnumOrtTalepDurumu.PERSONEL_ATAMASI_BEKLIYOR,
            EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR, "orttalep:guncelle", "pi pi-clipboard"),
    TALEP_REDET("Talebi Reddet", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.TALEP_REDDEDILDI, "orttalep:guncelle", "pi pi-times-circle"),
    UCRET_BELIRLE("Ücret Belirle", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.UCRET_BELIRLEME_BEKLIYOR, "orttalep:guncelle", "pi pi-money-bill"),
    SOZLESME_IMZALA("Sözleşme İmzala", EnumOrtTalepDurumu.UCRET_BELIRLEME_BEKLIYOR,
            EnumOrtTalepDurumu.SOZLESME_BEKLIYOR, "orttalep:guncelle", "pi pi-file-edit"),
    ODEME_AL("Ödeme Al", EnumOrtTalepDurumu.SOZLESME_BEKLIYOR,
            EnumOrtTalepDurumu.ODEME_BEKLIYOR, "orttalep:guncelle", "pi pi-wallet"),
    ODEME_TAMAMLA("Ödeme ve Dekontu Tamamla", EnumOrtTalepDurumu.ODEME_BEKLIYOR,
            EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR, "orttalep:guncelle", "pi pi-receipt"),
    PROJE_BELIRLE("Proje Belirle", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.PROJE_BELIRLENDI, "orttalep:guncelle", "pi pi-briefcase"),
    OLCU_RANDEVUSU("Ölçü Randevusu Oluştur", EnumOrtTalepDurumu.PROJE_BELIRLENDI,
            EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR, "orttalep:guncelle", "pi pi-calendar-plus"),
    OLCU_AL("Ölçü Alındı", EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.OLCU_ALINDI, "orttalep:guncelle", "pi pi-ruler"),
    FORMLARI_TAMAMLA("Ölçü Formlarını Tamamla", EnumOrtTalepDurumu.OLCU_ALINDI,
            EnumOrtTalepDurumu.FORMLAR_BEKLENIYOR, "orttalep:guncelle", "pi pi-file"),
    RAPOR_AL("Doktor Raporunu Al", EnumOrtTalepDurumu.FORMLAR_BEKLENIYOR,
            EnumOrtTalepDurumu.DOKTOR_RAPORU_BEKLENIYOR, "orttalep:guncelle", "pi pi-file-pdf"),
    RECETE_AL("Reçeteyi Al", EnumOrtTalepDurumu.DOKTOR_RAPORU_BEKLENIYOR,
            EnumOrtTalepDurumu.RECETE_BEKLENIYOR, "orttalep:guncelle", "pi pi-file-pdf"),
    MALZEME_TALEP_ET("Malzeme Talebi Oluştur", EnumOrtTalepDurumu.RECETE_BEKLENIYOR,
            EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR, "orttalep:guncelle", "pi pi-shopping-cart"),
    DEPO_ONAYINA_GONDER("Depo Onayına Gönder", EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR,
            EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR, "orttalep:guncelle", "pi pi-send"),
    PROVA_RANDEVUSU("Prova Randevusu Oluştur", EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR, "orttalep:guncelle", "pi pi-calendar-plus"),
    PROVA_YAP("Prova Yap", EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_YAPILDI, "orttalep:guncelle", "pi pi-check-circle"),
    YENIDEN_PROVA("Yeniden Prova Planla", EnumOrtTalepDurumu.PROVA_YAPILDI,
            EnumOrtTalepDurumu.YENIDEN_PROVA_BEKLIYOR, "orttalep:guncelle", "pi pi-refresh"),
    TESLIMATA_HAZIRLA("Teslimata Hazırla", EnumOrtTalepDurumu.PROVA_YAPILDI,
            EnumOrtTalepDurumu.TESLIMAT_BEKLIYOR, "orttalep:guncelle", "pi pi-truck"),
    YENIDEN_PROVA_RANDEVUSU("Yeniden Prova Randevusu", EnumOrtTalepDurumu.YENIDEN_PROVA_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR, "orttalep:guncelle", "pi pi-calendar-plus"),
    TESLIM_ET("Teslim Et", EnumOrtTalepDurumu.TESLIMAT_BEKLIYOR,
            EnumOrtTalepDurumu.TESLIM_EDILDI, "orttalep:guncelle", "pi pi-check"),
    TAMAMLA("Tamamla", EnumOrtTalepDurumu.TESLIM_EDILDI,
            EnumOrtTalepDurumu.TAMAMLANDI, "orttalep:guncelle", "pi pi-check-square"),
    KAPAT("Kapat", EnumOrtTalepDurumu.TAMAMLANDI,
            EnumOrtTalepDurumu.KAPATILDI, "orttalep:guncelle", "pi pi-lock"),
    KAPAT_RED("Reddedilen Talebi Kapat", EnumOrtTalepDurumu.TALEP_REDDEDILDI,
            EnumOrtTalepDurumu.KAPATILDI, "orttalep:guncelle", "pi pi-lock");

    private final String label;
    private final EnumOrtTalepDurumu sourceStatus;
    private final EnumOrtTalepDurumu targetStatus;
    private final String permission;
    private final String icon;

    /**
     * Randevu tamamlandığında doğrudan ilerlemeye izin verilen ek kaynak durumlar.
     */
    private static final Map<EnumOrtTalepIslem, Set<EnumOrtTalepDurumu>> EXTRA_SOURCES = Map.of(
            ON_MUAYENEYE_GEL, EnumSet.of(EnumOrtTalepDurumu.YENI_TALEP),
            OLCU_AL, EnumSet.of(EnumOrtTalepDurumu.PROJE_BELIRLENDI, EnumOrtTalepDurumu.ODEME_BEKLIYOR),
            // Randevu oluşturulmadan PROVA_RANDEVUSU_BEKLIYOR'a geçilmişse
            // (ör. eski depo onayı akışı) buradan kurtarma yolu açılır.
            PROVA_RANDEVUSU, EnumSet.of(EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR)
    );

    EnumOrtTalepIslem(String label, EnumOrtTalepDurumu sourceStatus, EnumOrtTalepDurumu targetStatus,
                      String permission, String icon) {
        this.label = label;
        this.sourceStatus = sourceStatus;
        this.targetStatus = targetStatus;
        this.permission = permission;
        this.icon = icon;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    /**
     * Bu işlemin uygulanabildiği tüm kaynak durumlar.
     */
    public Set<EnumOrtTalepDurumu> getSourceStatuses() {
        EnumSet<EnumOrtTalepDurumu> result = EnumSet.of(sourceStatus);
        result.addAll(EXTRA_SOURCES.getOrDefault(this, Set.of()));
        return result;
    }

    /**
     * İşleme karşılık gelen randevu tipi (randevu adımlarında dialog için).
     */
    public EnumOrtRandevuTuru getRandevuTuruForAction() {
        return switch (this) {
            case ON_MUAYENE_RANDEVUSU -> EnumOrtRandevuTuru.ON_MUAYENE;
            case OLCU_RANDEVUSU -> EnumOrtRandevuTuru.OLCU;
            case PROVA_RANDEVUSU -> EnumOrtRandevuTuru.PROVA;
            case YENIDEN_PROVA_RANDEVUSU -> EnumOrtRandevuTuru.YENIDEN_PROVA;
            default -> null;
        };
    }

    /**
     * Verilen durumda uygulanabilir işlemler.
     */
    public static Set<EnumOrtTalepIslem> getActions(EnumOrtTalepDurumu status) {
        if (status == null) {
            return EnumSet.noneOf(EnumOrtTalepIslem.class);
        }
        EnumSet<EnumOrtTalepIslem> result = EnumSet.noneOf(EnumOrtTalepIslem.class);
        for (EnumOrtTalepIslem action : values()) {
            if (action.getSourceStatuses().contains(status)) {
                result.add(action);
            }
        }
        return result;
    }
}
