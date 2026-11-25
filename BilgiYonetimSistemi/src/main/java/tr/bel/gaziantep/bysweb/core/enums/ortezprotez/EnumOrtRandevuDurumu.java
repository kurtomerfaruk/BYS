package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 08:23
 */
@Getter
public enum EnumOrtRandevuDurumu implements BaseEnum {

    PLANLANDI("Planlandı"),
    GELDI("Geldi"),
    GELMEDI("Gelmedi"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi");

    private final String label;

    EnumOrtRandevuDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
