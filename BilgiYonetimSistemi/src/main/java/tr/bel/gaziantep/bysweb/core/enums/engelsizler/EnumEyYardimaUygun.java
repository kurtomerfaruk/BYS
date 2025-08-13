package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:57
 */
public enum EnumEyYardimaUygun implements BaseEnum {

    ACIL("Acil"),
    NORMAL("Normal"),
    UYGUN_DEGIL("Uygun Değil");

    private final String label;

    EnumEyYardimaUygun(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

