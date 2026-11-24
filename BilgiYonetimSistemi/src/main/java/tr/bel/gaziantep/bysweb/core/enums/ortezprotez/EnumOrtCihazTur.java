package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 13:18
 */
@Getter
public enum EnumOrtCihazTur implements BaseEnum {

    ORTEZ("Ortez"),
    PROTEZ("Protez");

    private final String label;

    EnumOrtCihazTur(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

