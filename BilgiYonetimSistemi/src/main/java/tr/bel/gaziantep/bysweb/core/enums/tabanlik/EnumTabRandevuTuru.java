package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:24
 */
@Getter
public enum EnumTabRandevuTuru implements BaseEnum {

    OLCU("Ölçüm"),
    TESLIMAT("Teslimat");

    private final String label;

    EnumTabRandevuTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
