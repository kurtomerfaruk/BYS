package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:24
 */
@Getter
public enum EnumHfIsTuru implements BaseEnum {

    IHALELI_ISLER("İhaleli İşler"),
    OZEL_ISLER("Özel İşler");

    private final String label;

    EnumHfIsTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
