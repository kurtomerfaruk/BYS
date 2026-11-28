package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Getter
public enum EnumTabTalepIslem implements BaseEnum {

    RANDEVU_VER("Ölçüm Randevusu Ver", EnumTabTalepDurumu.YENI,
            EnumTabTalepDurumu.RANDEVU_BEKLIYOR, "tabtalep:guncelle", "pi pi-calendar-plus"),
    OLCUM_GIR("Ölçüm Gir", EnumTabTalepDurumu.RANDEVU_BEKLIYOR,
            EnumTabTalepDurumu.OLCUM_BEKLIYOR, "tabtalep:guncelle", "pi pi-ruler"),
    OLCUM_TAMAMLA("Ölçümü Tamamla", EnumTabTalepDurumu.OLCUM_BEKLIYOR,
            EnumTabTalepDurumu.URETIM_KARARI_BEKLIYOR, "tabtalep:guncelle", "pi pi-check-circle"),
    KARAR_EVET("Üretime Uygun", EnumTabTalepDurumu.URETIM_KARARI_BEKLIYOR,
            EnumTabTalepDurumu.UCRET_BEKLIYOR, "tabtalep:guncelle", "pi pi-check"),
    KARAR_HAYIR("Uygun Bulunmadı", EnumTabTalepDurumu.URETIM_KARARI_BEKLIYOR,
            EnumTabTalepDurumu.UYGUN_BULUNMADI, "tabtalep:guncelle", "pi pi-times-circle"),
    UCRETLI_ODEME_ONAYLA("Ödeme Onayla", EnumTabTalepDurumu.UCRET_BEKLIYOR,
            EnumTabTalepDurumu.URETIM_BEKLIYOR, "tabtalep:guncelle", "pi pi-receipt"),
    UCRETSIZ_ONAYLA("Ücretsiz İşlemi Onayla", EnumTabTalepDurumu.UCRET_BEKLIYOR,
            EnumTabTalepDurumu.URETIM_BEKLIYOR, "tabtalep:guncelle", "pi pi-check-circle"),
    URETIM_BASLAT("Üretime Başla", EnumTabTalepDurumu.URETIM_BEKLIYOR,
            EnumTabTalepDurumu.URETIMDE, "tabtalep:guncelle", "pi pi-cog"),
    URETIM_TAMAMLA("Üretimi Tamamla", EnumTabTalepDurumu.URETIMDE,
            EnumTabTalepDurumu.TESLIMAT_RANDEVUSU_BEKLIYOR, "tabtalep:guncelle", "pi pi-check-square"),
    TESLIMAT_RANDEVUSU_VER("Teslimat Randevusu Ver", EnumTabTalepDurumu.TESLIMAT_RANDEVUSU_BEKLIYOR,
            EnumTabTalepDurumu.TESLIMAT_BEKLIYOR, "tabtalep:guncelle", "pi pi-calendar-plus"),
    TESLIM_ET("Teslim Et", EnumTabTalepDurumu.TESLIMAT_BEKLIYOR,
            EnumTabTalepDurumu.TAMAMLANDI, "tabtalep:guncelle", "pi pi-check");

    private final String label;
    private final EnumTabTalepDurumu sourceStatus;
    private final EnumTabTalepDurumu targetStatus;
    private final String permission;
    private final String icon;

    EnumTabTalepIslem(String label, EnumTabTalepDurumu sourceStatus, EnumTabTalepDurumu targetStatus,
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

    public Set<EnumTabTalepDurumu> getSourceStatuses() {
        return EnumSet.of(sourceStatus);
    }


    /**
     * Randevu gerektiren işlemlerin açacağı randevu türü (Ortez-Protez
     * {@code getRandevuTuruForAction()} kalıbı).
     */
    public EnumTabRandevuTuru getRandevuTuruForAction() {
        return switch (this) {
            case RANDEVU_VER -> EnumTabRandevuTuru.OLCU;
            case TESLIMAT_RANDEVUSU_VER -> EnumTabRandevuTuru.TESLIMAT;
            default -> null;
        };
    }

    public static Set<EnumTabTalepIslem> getActions(EnumTabTalepDurumu durum) {
        if (durum == null) {
            return EnumSet.noneOf(EnumTabTalepIslem.class);
        }
        EnumSet<EnumTabTalepIslem> result = EnumSet.noneOf(EnumTabTalepIslem.class);
        for (EnumTabTalepIslem action : values()) {
            if (action.getSourceStatuses().contains(durum)) {
                result.add(action);
            }
        }
        return result;
    }
}
