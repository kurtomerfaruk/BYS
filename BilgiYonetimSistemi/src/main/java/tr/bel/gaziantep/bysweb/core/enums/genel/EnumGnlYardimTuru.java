package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:32
 */
@Getter
public enum EnumGnlYardimTuru implements BaseEnum {

    NAKIT("Nakit"),
    GIDA("Gıda"),
    AS_EVI("Aş Evi"),
    YAKACAK("Yakacak");

    private final String label;

    EnumGnlYardimTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
