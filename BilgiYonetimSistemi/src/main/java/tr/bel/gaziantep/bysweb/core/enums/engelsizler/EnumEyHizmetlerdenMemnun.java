package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:56
 */
@Getter
public enum EnumEyHizmetlerdenMemnun implements BaseEnum {

    EVET("Evet"),
    HAYIR("Hayır"),
    BILMIYORUM("Bilmiyorum"),
    KARARSIZ("Kararsız");

    private final String label;

    EnumEyHizmetlerdenMemnun(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
