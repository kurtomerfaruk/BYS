package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 2.10.2026 11:10
 */
@Getter
public enum EnumPkModul implements BaseEnum {

    REVIR("Revir"),
    OPIYAT("Opiyat");

    private final String label;

    EnumPkModul(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}