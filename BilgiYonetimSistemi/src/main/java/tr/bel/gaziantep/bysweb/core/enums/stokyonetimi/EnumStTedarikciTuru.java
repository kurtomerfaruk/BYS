package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 13:53
 */
@Getter
public enum EnumStTedarikciTuru implements BaseEnum {

    BELEDIYE("Belediye"),
    HAYIRSEVER("Hayırsever"),
    DERNEK("Dernek");

    private final String label;

    EnumStTedarikciTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
