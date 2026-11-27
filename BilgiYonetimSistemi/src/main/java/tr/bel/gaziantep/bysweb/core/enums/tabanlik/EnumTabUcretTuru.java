package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Getter
public enum EnumTabUcretTuru implements BaseEnum {

    UCRETLI("Ücretli"),
    UCRETSIZ("Ücretsiz");

    private final String label;

    EnumTabUcretTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
