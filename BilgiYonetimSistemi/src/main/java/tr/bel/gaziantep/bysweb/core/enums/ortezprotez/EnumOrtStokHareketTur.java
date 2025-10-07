package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 08:40
 */
@Getter
public enum EnumOrtStokHareketTur implements BaseEnum {

    HASTA_ICIN_KULLANIM("Hasta için kullanım");

    private final String label;

    EnumOrtStokHareketTur(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}