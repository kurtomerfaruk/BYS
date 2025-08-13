package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:40
 */
@Getter
public enum EnumSyFiltreTuru implements BaseEnum {

    INPUT("Input"),
    SELECT_ONE_MENU("Select One Menu"),
    DATE_PICKER("Date Picker"),
    RATING("Rating");

    private final String label;

    EnumSyFiltreTuru(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
