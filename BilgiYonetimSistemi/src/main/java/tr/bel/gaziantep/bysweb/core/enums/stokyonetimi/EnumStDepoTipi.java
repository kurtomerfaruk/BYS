package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStDepoTipi implements BaseEnum {
    MERKEZ("Merkez Depo"),
    SUBE("Şube Depo"),
    TRANSIT("Transit Depo"),
    SANAL("Sanal Depo");

    private final String label;

    EnumStDepoTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
