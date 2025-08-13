package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 12:04
 */
@Getter
public enum EnumEyAracBilgisi implements BaseEnum {

    AKULU("Akülü"),
    MANUEL("Manuel");

    private final String label;

    EnumEyAracBilgisi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}