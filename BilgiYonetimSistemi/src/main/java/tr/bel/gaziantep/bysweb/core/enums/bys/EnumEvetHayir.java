package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 08:57
 */
@Getter
public enum EnumEvetHayir implements BaseEnum {
    EVET("Evet"),
    HAYIR("Hayır");

    private final String label;

    EnumEvetHayir(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
