package tr.bel.gaziantep.bysweb.core.enums.moralevi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 21.11.2025 08:15
 */
@Getter
public enum EnumMeAnketHizmetSuresi implements BaseEnum {

    BIR_AYDAN_AZ("1 aydan az"),
    BIR_ALTI_AY("1-6 ay"),
    ALTI_ONIKI_AY("6-12 ay"),
    BIR_YILDAN_FAZLA("1 yıldan fazla");

    private final String label;

    EnumMeAnketHizmetSuresi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
