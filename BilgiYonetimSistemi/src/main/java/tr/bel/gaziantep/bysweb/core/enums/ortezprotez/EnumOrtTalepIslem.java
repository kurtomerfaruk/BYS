package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:44
 */
@Getter
public enum EnumOrtTalepIslem implements BaseEnum {

    ON_MUAYENE_RANDEVUSU("Ön Muayene Randevusu Oluştur", EnumOrtTalepDurumu.YENI_TALEP,
            EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR, "orttalep:randevu", "pi pi-calendar-plus"),
    ON_MUAYENEYE_GEL("Ön Muayeneye Geldi", EnumOrtTalepDurumu.ON_MUAYENE_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.ON_MUAYENEYE_GELDI, "orttalep:onmuayene", "pi pi-check-circle"),
    PERSONEL_ATA("Personel Ataması Yap", EnumOrtTalepDurumu.ON_MUAYENEYE_GELDI,
            EnumOrtTalepDurumu.PERSONEL_ATAMASI_BEKLIYOR, "orttalep:atama", "pi pi-users"),
    DEGERLENDIRMEYE_AL("Değerlendirmeye Al", EnumOrtTalepDurumu.PERSONEL_ATAMASI_BEKLIYOR,
            EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR, "orttalep:degerlendirme", "pi pi-clipboard"),
    TALEP_REDET("Talebi Reddet", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.TALEP_REDDEDILDI, "orttalep:degerlendirme", "pi pi-times-circle"),
    UCRET_BELIRLE("Ücret Belirle", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.UCRET_BELIRLEME_BEKLIYOR, "orttalep:odeme", "pi pi-money-bill"),
    SOZLESME_IMZALA("Sözleşme İmzala", EnumOrtTalepDurumu.UCRET_BELIRLEME_BEKLIYOR,
            EnumOrtTalepDurumu.SOZLESME_BEKLIYOR, "orttalep:sozlesme", "pi pi-file-edit"),
    ODEME_AL("Ödeme Al", EnumOrtTalepDurumu.SOZLESME_BEKLIYOR,
            EnumOrtTalepDurumu.ODEME_BEKLIYOR, "orttalep:odeme", "pi pi-wallet"),
    ODEME_TAMAMLA("Ödeme ve Dekontu Tamamla", EnumOrtTalepDurumu.ODEME_BEKLIYOR,
            EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR, "orttalep:odeme", "pi pi-receipt"),
    PROJE_BELIRLE("Proje Belirle", EnumOrtTalepDurumu.DEGERLENDIRME_BEKLIYOR,
            EnumOrtTalepDurumu.PROJE_BELIRLENDI, "orttalep:proje", "pi pi-briefcase"),
    OLCU_RANDEVUSU("Ölçü Randevusu Oluştur", EnumOrtTalepDurumu.PROJE_BELIRLENDI,
            EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR, "orttalep:randevu", "pi pi-calendar-plus"),
    OLCU_AL("Ölçü Alındı", EnumOrtTalepDurumu.OLCU_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.OLCU_ALINDI, "orttalep:olcu", "pi pi-ruler"),
    FORMLARI_TAMAMLA("Ölçü Formlarını Tamamla", EnumOrtTalepDurumu.OLCU_ALINDI,
            EnumOrtTalepDurumu.FORMLAR_BEKLENIYOR, "orttalep:belge", "pi pi-file"),
    RAPOR_AL("Doktor Raporunu Al", EnumOrtTalepDurumu.FORMLAR_BEKLENIYOR,
            EnumOrtTalepDurumu.DOKTOR_RAPORU_BEKLENIYOR, "orttalep:belge", "pi pi-file-pdf"),
    RECETE_AL("Reçeteyi Al", EnumOrtTalepDurumu.DOKTOR_RAPORU_BEKLENIYOR,
            EnumOrtTalepDurumu.RECETE_BEKLENIYOR, "orttalep:belge", "pi pi-file-pdf"),
    MALZEME_TALEP_ET("Malzeme Talebi Oluştur", EnumOrtTalepDurumu.RECETE_BEKLENIYOR,
            EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR, "orttalep:malzeme", "pi pi-shopping-cart"),
    DEPO_ONAYINA_GONDER("Depo Onayına Gönder", EnumOrtTalepDurumu.MALZEME_TALEBI_BEKLIYOR,
            EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR, "orttalep:malzeme", "pi pi-send"),
    PROVA_RANDEVUSU("Prova Randevusu Oluştur", EnumOrtTalepDurumu.DEPO_ONAYI_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR, "orttalep:randevu", "pi pi-calendar-plus"),
    PROVA_YAP("Prova Yap", EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_YAPILDI, "orttalep:prova", "pi pi-check-circle"),
    YENIDEN_PROVA("Yeniden Prova Planla", EnumOrtTalepDurumu.PROVA_YAPILDI,
            EnumOrtTalepDurumu.YENIDEN_PROVA_BEKLIYOR, "orttalep:prova", "pi pi-refresh"),
    TESLIMATA_HAZIRLA("Teslimata Hazırla", EnumOrtTalepDurumu.PROVA_YAPILDI,
            EnumOrtTalepDurumu.TESLIMAT_BEKLIYOR, "orttalep:teslim", "pi pi-truck"),
    YENIDEN_PROVA_RANDEVUSU("Yeniden Prova Randevusu", EnumOrtTalepDurumu.YENIDEN_PROVA_BEKLIYOR,
            EnumOrtTalepDurumu.PROVA_RANDEVUSU_BEKLIYOR, "orttalep:randevu", "pi pi-calendar-plus"),
    TESLIM_ET("Teslim Et", EnumOrtTalepDurumu.TESLIMAT_BEKLIYOR,
            EnumOrtTalepDurumu.TESLIM_EDILDI, "orttalep:teslim", "pi pi-check"),
    TAMAMLA("Tamamla", EnumOrtTalepDurumu.TESLIM_EDILDI,
            EnumOrtTalepDurumu.TAMAMLANDI, "orttalep:teslim", "pi pi-check-square"),
    KAPAT("Kapat", EnumOrtTalepDurumu.TAMAMLANDI,
            EnumOrtTalepDurumu.KAPATILDI, "orttalep:guncelle", "pi pi-lock"),
    KAPAT_RED("Reddedilen Talebi Kapat", EnumOrtTalepDurumu.TALEP_REDDEDILDI,
            EnumOrtTalepDurumu.KAPATILDI, "orttalep:guncelle", "pi pi-lock");

    private final String label;
    private final EnumOrtTalepDurumu kaynakDurum;
    private final EnumOrtTalepDurumu hedefDurum;
    private final String yetki;
    private final String ikon;

    EnumOrtTalepIslem(String label, EnumOrtTalepDurumu kaynakDurum, EnumOrtTalepDurumu hedefDurum,
                      String yetki, String ikon) {
        this.label = label;
        this.kaynakDurum = kaynakDurum;
        this.hedefDurum = hedefDurum;
        this.yetki = yetki;
        this.ikon = ikon;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    /**
     * Verilen durumda uygulanabilir işlemler.
     */
    public static Set<EnumOrtTalepIslem> getIslemler(EnumOrtTalepDurumu durum) {
        if (durum == null) {
            return EnumSet.noneOf(EnumOrtTalepIslem.class);
        }
        EnumSet<EnumOrtTalepIslem> sonuc = EnumSet.noneOf(EnumOrtTalepIslem.class);
        for (EnumOrtTalepIslem islem : values()) {
            if (islem.getKaynakDurum() == durum) {
                sonuc.add(islem);
            }
        }
        return sonuc;
    }
}
