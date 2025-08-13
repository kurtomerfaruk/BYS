package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 11:18
 */
@Getter
public enum EnumHfAracTuru implements BaseEnum {

    KAMYON("Kamyon"),
    KAMYONET("Kamyonet"),
    TRAKTOR("Traktör");

    private final String label;

    EnumHfAracTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}