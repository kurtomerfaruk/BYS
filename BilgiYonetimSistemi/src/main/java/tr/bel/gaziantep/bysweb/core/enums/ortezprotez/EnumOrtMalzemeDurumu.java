package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 25.11.2026 16:03
 */
@Getter
public enum EnumOrtMalzemeDurumu implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    ONAYLANDI("Onaylandı"),
    REDDEDILDI("Reddedildi"),
    IPTAL_EDILDI("İptal Edildi");

    private final String label;

    EnumOrtMalzemeDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

