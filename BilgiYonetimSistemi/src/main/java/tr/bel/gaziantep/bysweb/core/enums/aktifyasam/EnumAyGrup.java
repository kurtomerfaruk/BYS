package tr.bel.gaziantep.bysweb.core.enums.aktifyasam;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.11.2025 11:27
 */
@Getter
public enum EnumAyGrup implements BaseEnum {

    BIRINCI_GRUP("1. Grup"),
    IKINCI_GRUP("2. Grup");

    private final String label;

    EnumAyGrup(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }


}