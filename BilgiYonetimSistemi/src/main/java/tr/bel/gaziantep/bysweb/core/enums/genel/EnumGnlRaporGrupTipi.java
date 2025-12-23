package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.12.2025 09:51
 */
@Getter
public enum EnumGnlRaporGrupTipi implements BaseEnum {

    HEADER("Header"),
    FOOTER("Footer"),
    SUBTOTAL("Subtotal");

    private final String label;

    EnumGnlRaporGrupTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}