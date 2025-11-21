package tr.bel.gaziantep.bysweb.core.enums.moralevi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 21.11.2025 08:13
 */
@Getter
public enum EnumMeAnketPeriyot implements BaseEnum {

    ILK_PERIYOT("İlk Periyot"),
    IKINCI_PERIYOT("İkinci Periyot");

    private final String label;

    EnumMeAnketPeriyot(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
