package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 13:44
 */
@Getter
public enum EnumOrtSablonAlanTuru implements BaseEnum {

    TEXT("Text"),
    NUMBER("Number"),
    DATE("Date"),
    CHECKBOX("Checkbox"),
    SELECT("Select");

    private final String label;

    EnumOrtSablonAlanTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}