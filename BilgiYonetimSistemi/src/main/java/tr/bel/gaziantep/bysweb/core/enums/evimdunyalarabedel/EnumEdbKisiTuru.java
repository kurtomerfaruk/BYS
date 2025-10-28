package tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:46
 */
@Getter
public enum EnumEdbKisiTuru implements BaseEnum {
    ENGELLI("Engelli"),
    YASLI("Yaşlı"),
    ENGELLI_YASLI("Engelli/Yaşlı");

    private final String label;

    EnumEdbKisiTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
