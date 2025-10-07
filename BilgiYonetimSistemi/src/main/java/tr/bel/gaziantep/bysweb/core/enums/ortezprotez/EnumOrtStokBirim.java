package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.10.2025 14:03
 */
@Getter
public enum EnumOrtStokBirim implements BaseEnum {

    ADET("Adet"),
    Metre("Metre");

    private final String label;

    EnumOrtStokBirim(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}