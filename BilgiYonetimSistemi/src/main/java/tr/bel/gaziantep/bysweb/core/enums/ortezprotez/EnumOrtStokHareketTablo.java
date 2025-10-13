package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.10.2025 10:56
 */
@Getter
public enum EnumOrtStokHareketTablo implements BaseEnum {

    ORTBASVURU_MALZEME_TESLIMI("Malzeme Teslimi"),
    ORTMALZEME_TALEP("Malzeme Talep");

    private final String label;

    EnumOrtStokHareketTablo(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}