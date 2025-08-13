package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:38
 */
@Getter
public enum EnumSyVeriTipi implements BaseEnum {

    ID("Id"),
    DATE_TIME("Datetime"),
    DATE("Date"),
    BOOLEAN("Boolean"),
    ENUM("Enum"),
    STRING("String"),
    INTEGER("Integer"),
    DECIMAL("Decimal");

    private final String label;

    EnumSyVeriTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
