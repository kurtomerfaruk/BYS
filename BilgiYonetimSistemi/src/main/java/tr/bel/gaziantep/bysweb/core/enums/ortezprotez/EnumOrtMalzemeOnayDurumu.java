package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 10:16
 */
@Getter
public enum EnumOrtMalzemeOnayDurumu implements BaseEnum {

    BEKLEMEDE("Beklemede"),
    ONAYLANDI("Onaylandı"),
    REDDEDILDI("Reddedildi"),
    IPTAL_EDILDI("İptal Edildi");

    private final String label;

    EnumOrtMalzemeOnayDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}