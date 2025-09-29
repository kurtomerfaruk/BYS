package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.09.2025 11:38
 */
@Getter
public enum EnumOrtBasvuruDurumu implements BaseEnum {

    OLUMLU("Olumlu"),
    OLUMSUZ("Olumsuz"),
    IPTAL("İptal");

    private final String label;

    EnumOrtBasvuruDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}