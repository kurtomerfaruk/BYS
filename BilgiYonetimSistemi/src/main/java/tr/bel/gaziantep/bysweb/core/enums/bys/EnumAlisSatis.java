package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.10.2025 09:18
 */
@Getter
public enum EnumAlisSatis implements BaseEnum {
    ALIS("Alış"),
    SATIS("Satış");

    private final String label;

    EnumAlisSatis(String label) {
        this.label = label;
    }
    @Override
    public String getDisplayValue() {
        return label;
    }
}
