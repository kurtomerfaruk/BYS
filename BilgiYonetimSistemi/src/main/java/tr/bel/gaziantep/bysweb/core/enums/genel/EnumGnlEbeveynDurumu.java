package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:58
 */
@Getter
public enum EnumGnlEbeveynDurumu implements BaseEnum {

    ANNE_BABA_BIRLIKTE("Anne-Baba Birlikte"),
    ANNE_BABA_VEFAT("Anne-Baba Vefat"),
    ANNE_SAG_BABA_VEFAT("Anne Sağ Baba Vefat"),
    ANNE_VEFAT_BABA_SAG("Anne Vefat Baba Sağ"),
    ANNE_BABA_AYRI("Anne-Baba Ayrı"),
    ANNE_BABA_BOSANMIS("Anne Baba Boşanmış");

    private final String label;

    EnumGnlEbeveynDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

