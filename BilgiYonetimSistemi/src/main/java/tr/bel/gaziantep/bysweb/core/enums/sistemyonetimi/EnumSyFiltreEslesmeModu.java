package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:27
 */
@Getter
public enum EnumSyFiltreEslesmeModu implements BaseEnum {
    STARTS_WITH("startsWith"),
    NOT_STARTS_WITH("notStartsWith"),
    ENDS_WITH("endsWith"),
    NOT_ENDS_WITH("notEndsWith"),
    CONTAINS("contains"),
    NOT_CONTAINS("notContains"),
    EXACT("exact"),
    NOT_EXACT("notExact"),
    LESS_THAN("lt"),
    LESS_THAN_EQUALS("lte"),
    GREATER_THAN("gt"),
    GREATER_THAN_EQUALS("gte"),
    EQUALS("equals"),
    NOT_EQUALS("notEquals"),
    IN("in"),
    NOT_IN("notIn"),
    BETWEEN("between"),
    NOT_BETWEEN("notBetween"),
    GLOBAL("global");

    private final String label;

    EnumSyFiltreEslesmeModu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
