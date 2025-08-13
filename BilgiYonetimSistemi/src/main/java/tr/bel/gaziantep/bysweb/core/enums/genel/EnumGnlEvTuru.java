package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:12
 */
@Getter
public enum EnumGnlEvTuru implements BaseEnum {

    MUSTAKIL("Müstakil"),
    APARTMAN_DAIRESI("Apartman Dairesi"),
    BARAKA("Baraka"),
    BODRUM_KAT("Bodrum Kat"),
    GECE_KONDU("Gece Kondu");

    private final String label;

    EnumGnlEvTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}
