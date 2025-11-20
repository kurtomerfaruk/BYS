package tr.bel.gaziantep.bysweb.core.enums.moralevi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 11:03
 */
@Getter
public enum EnumMeTelefonKullanimDurumu implements BaseEnum {

    KULLANIYOR("Kullanıyor"),
    KULLANMIYOR("Kullanmıyor");

    private final String label;

    EnumMeTelefonKullanimDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}