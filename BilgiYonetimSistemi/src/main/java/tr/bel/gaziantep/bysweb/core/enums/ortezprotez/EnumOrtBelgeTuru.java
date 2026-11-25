package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 14:02
 */
@Getter
public enum EnumOrtBelgeTuru implements BaseEnum {

    OLCU_FORMU("Ölçü Formu"),
    RAPOR("Doktor Raporu"),
    RECETE("Reçete"),
    SOZLESME("Hasta Sözleşmesi"),
    DEKONT("Ödeme Dekontu"),
    TESLIM_TUTANAGI("Teslim Tutanağı"),
    DIGER("Diğer");

    private final String label;

    EnumOrtBelgeTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
