package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:10
 */
@Getter
public enum EnumGnlKiminleYasiyor implements BaseEnum {

    ESI("Eşi"),
    AILE("Aile"),
    YAKINI("Yakını"),
    COCUGU("Çocuğu"),
    YALNIZ("Yalnız");

    private final String label;

    EnumGnlKiminleYasiyor(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}