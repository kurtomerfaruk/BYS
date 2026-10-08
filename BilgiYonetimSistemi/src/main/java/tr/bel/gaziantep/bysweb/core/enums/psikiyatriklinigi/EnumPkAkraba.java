package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 28.09.2026 10:09
 */
@Getter
public enum EnumPkAkraba implements BaseEnum {

    ANNE("Anne"),
    BABA("Baba"),
    KARDES("Kardeş"),
    AMCA_HALA("Amca/Hala"),
    DAYI_TEYZE("Dayı/Teyze"),
    KUZEN("Kuzen"),
    DIGER("Diğer");

    private final String label;

    EnumPkAkraba(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}