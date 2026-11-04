package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.26.0
 * @since 3.11.2026 13:48
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
