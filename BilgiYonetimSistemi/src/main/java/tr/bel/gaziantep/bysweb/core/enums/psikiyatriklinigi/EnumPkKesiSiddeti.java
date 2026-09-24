package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 14:40
 */
@Getter
public enum EnumPkKesiSiddeti implements BaseEnum {

    AZ("Az"),
    ORTA("Orta"),
    SIDDETLI("Şiddetli");

    private final String label;

    EnumPkKesiSiddeti(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}