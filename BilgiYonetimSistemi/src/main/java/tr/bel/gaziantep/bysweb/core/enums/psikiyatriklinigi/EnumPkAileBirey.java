package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 13:55
 */
@Getter
public enum EnumPkAileBirey implements BaseEnum {

    ANNE("Anne"),
    BABA("Baba"),
    KARDES("Kardeş");

    private final String label;

    EnumPkAileBirey(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
