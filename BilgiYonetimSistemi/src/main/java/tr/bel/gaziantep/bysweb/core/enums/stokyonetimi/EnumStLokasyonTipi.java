package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStLokasyonTipi implements BaseEnum {
    KABUL("Kabul"),
    DEPOLAMA("Depolama"),
    SEVK("Sevk"),
    KARANTINA("Karantina"),
    FIRE("Fire/Zayi");

    private final String label;

    EnumStLokasyonTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
