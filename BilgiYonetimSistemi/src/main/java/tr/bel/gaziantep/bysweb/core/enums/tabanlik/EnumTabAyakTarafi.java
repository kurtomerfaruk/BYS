package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:56
 */
@Getter
public enum EnumTabAyakTarafi implements BaseEnum {

    SAG("Sağ"),
    SOL("Sol");

    private final String label;

    EnumTabAyakTarafi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
