package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 13:49
 */
@Getter
public enum EnumPkAileDurum  implements BaseEnum {

    BIRLIKTE_YASIYOR("Birlikte yaşıyor"),
    BOSANMIS_BIRLIKTE_YASIYOR("Boşanmış birlikte yaşıyor"),
    BOSANMIS_AYRI_YASIYOR("Boşanmış ayrı yaşıyor"),
    ANNE_VEFAT("Anne vefat"),
    BABA_VEFAT("Baba vefat"),
    ANNENIN_IKINCI_EVLILIGI_VAR("Annenin ikinci evliliği var"),
    BABANIN_IKINCI_EVLILIGI_VAR("Babanın ikinci evliliği var"),
    ANNE_BABA_VEFAT("Anne-Baba vefat");

    private final String label;

    EnumPkAileDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}