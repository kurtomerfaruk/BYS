package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 25.09.2026 10:48
 */
@Getter
public enum EnumPkTedaviSekli implements BaseEnum {

    AYAKTAN("Ayaktan"),
    YATILI("Yatılı");

    private final String label;

    EnumPkTedaviSekli(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
