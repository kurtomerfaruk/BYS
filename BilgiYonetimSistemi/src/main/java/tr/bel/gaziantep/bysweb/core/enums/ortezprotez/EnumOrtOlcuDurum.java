package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.10.2025 13:38
 */
@Getter
public enum EnumOrtOlcuDurum implements BaseEnum {

    BEKLEMEDE("Beklemede"),
    UYGUN("Uygun"),
    UYGUN_DEGIL("Uygun Değil");

    private final String label;

    EnumOrtOlcuDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}