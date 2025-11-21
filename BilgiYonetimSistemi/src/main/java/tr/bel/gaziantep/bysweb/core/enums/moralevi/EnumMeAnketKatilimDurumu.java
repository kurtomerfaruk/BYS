package tr.bel.gaziantep.bysweb.core.enums.moralevi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 21.11.2025 08:17
 */
@Getter
public enum EnumMeAnketKatilimDurumu implements BaseEnum {

    BIR_GUN("1 gün"),
    IKI_GUN("2 gün");

    private final String label;

    EnumMeAnketKatilimDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
