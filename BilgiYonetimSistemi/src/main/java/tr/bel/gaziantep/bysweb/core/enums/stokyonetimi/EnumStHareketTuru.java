package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStHareketTuru implements BaseEnum {
    SATIN_ALMA_GIRISI("Satın Alma Girişi"),
    SATIS_CIKISI("Satış Çıkışı"),
    DEPO_TRANSFERI("Depolar Arası Transfer"),
    SAYIM_FAZLASI("Sayım Fazlası"),
    SAYIM_EKSIGI("Sayım Eksiği"),
    IADE_GIRISI("İade Girişi"),
    IADE_CIKISI("İade Çıkışı"),
    FIRE("Fire"),
    ZAYI("Zayi"),
    SARF("Sarf"),
    ACILIS_STOGU("Açılış Stoğu"),
    MANUEL_DUZELTME("Manuel Düzeltme");

    private final String label;

    EnumStHareketTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
