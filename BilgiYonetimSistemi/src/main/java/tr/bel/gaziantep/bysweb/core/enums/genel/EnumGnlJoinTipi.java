package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 12:56
 */
@Getter
public enum EnumGnlJoinTipi implements BaseEnum {

    INNER("INNER"),
    LEFT("LEFT"),
    RIGHT("RIGHT");

    private final String label;

    EnumGnlJoinTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
