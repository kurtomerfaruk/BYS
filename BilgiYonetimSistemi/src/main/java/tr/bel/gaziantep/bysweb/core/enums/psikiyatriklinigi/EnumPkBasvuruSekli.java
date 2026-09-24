package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 12:59
 */
@Getter
public enum EnumPkBasvuruSekli implements BaseEnum {

    AILE_ILE_BIRLIKTE("Aile ile birlikte"),
    RESMI_KURUM_ARACILIGI_ILE("Resmi Kurum aracılığı ile"),
    MOBIL_EKIP("Mobil Ekip"),
    DIGER("Diğer");

    private final String label;

    EnumPkBasvuruSekli(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
