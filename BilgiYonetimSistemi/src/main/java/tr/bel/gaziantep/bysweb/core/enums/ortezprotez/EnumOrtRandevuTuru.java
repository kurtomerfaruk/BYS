package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 08:23
 */
@Getter
public enum EnumOrtRandevuTuru implements BaseEnum {

    ON_MUAYENE("Ön Muayene"),
    OLCU("Ölçü"),
    PROVA("Prova"),
    YENIDEN_PROVA("Yeniden Prova"),
    FIZIK_TEDAVI("Fizik Tedavi"),
    TABANLIK("Tabanlık");

    private final String label;

    EnumOrtRandevuTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

