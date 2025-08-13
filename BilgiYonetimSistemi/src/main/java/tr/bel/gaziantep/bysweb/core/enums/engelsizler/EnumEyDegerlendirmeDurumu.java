package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.06.2025 11:40
 */
@Getter
public enum EnumEyDegerlendirmeDurumu implements BaseEnum {

    OLUMLU("Olumlu"),
    OLUMSUZ("Olumsuz");

    private final String label;

    EnumEyDegerlendirmeDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}