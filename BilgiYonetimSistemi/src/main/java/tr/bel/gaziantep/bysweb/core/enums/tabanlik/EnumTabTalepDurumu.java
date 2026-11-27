package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:35
 */
@Getter
public enum EnumTabTalepDurumu implements BaseEnum {

    YENI("Yeni"),
    RANDEVU_BEKLIYOR("Randevu Bekliyor"),
    OLCUM_BEKLIYOR("Ölçüm Bekliyor"),
    URETIM_KARARI_BEKLIYOR("Üretim Kararı Bekliyor"),
    UCRET_BEKLIYOR("Ödeme Bekliyor"),
    URETIM_BEKLIYOR("Üretim Bekliyor"),
    URETIMDE("Üretimde"),
    TESLIMAT_RANDEVUSU_BEKLIYOR("Teslimat Randevusu Bekliyor"),
    TESLIMAT_BEKLIYOR("Teslimat Bekliyor"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi"),
    UYGUN_BULUNMADI("Uygun Bulunmadı");

    private final String label;

    /**
     * Geri dönüşte hedef alınabilecek önceki durumlar.
     */
    private static final Map<EnumTabTalepDurumu, Set<EnumTabTalepDurumu>> PREVIOUS = Map.ofEntries(
            Map.entry(RANDEVU_BEKLIYOR, EnumSet.of(YENI)),
            Map.entry(OLCUM_BEKLIYOR, EnumSet.of(RANDEVU_BEKLIYOR)),
            Map.entry(URETIM_KARARI_BEKLIYOR, EnumSet.of(OLCUM_BEKLIYOR)),
            Map.entry(UCRET_BEKLIYOR, EnumSet.of(URETIM_KARARI_BEKLIYOR)),
            Map.entry(URETIM_BEKLIYOR, EnumSet.of(UCRET_BEKLIYOR)),
            Map.entry(URETIMDE, EnumSet.of(URETIM_BEKLIYOR)),
            Map.entry(TESLIMAT_RANDEVUSU_BEKLIYOR, EnumSet.of(URETIMDE)),
            Map.entry(TESLIMAT_BEKLIYOR, EnumSet.of(TESLIMAT_RANDEVUSU_BEKLIYOR))
    );

    EnumTabTalepDurumu(String label) {
        this.label = label;
    }

    public Set<EnumTabTalepDurumu> getPreviousStatuses() {
        return PREVIOUS.getOrDefault(this, EnumSet.noneOf(EnumTabTalepDurumu.class));
    }

    public boolean canGoBack() {
        return !getPreviousStatuses().isEmpty();
    }

    /**
     * Karar aşamasından sonraki taleplerde genel bilgi güncellemesi kilitlidir;
     * değişiklikler workflow işlemleriyle yapılır.
     */
    public boolean isUpdateLocked() {
        return ordinal() >= UCRET_BEKLIYOR.ordinal();
    }

    public boolean isClosed() {
        return this == TAMAMLANDI || this == IPTAL_EDILDI || this == UYGUN_BULUNMADI;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

