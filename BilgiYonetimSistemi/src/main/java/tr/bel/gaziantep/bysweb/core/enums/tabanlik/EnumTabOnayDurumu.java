package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Getter
public enum EnumTabOnayDurumu implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    ONAYLANDI("Onaylandı"),
    RED("Reddedildi");

    private final String label;

    EnumTabOnayDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
