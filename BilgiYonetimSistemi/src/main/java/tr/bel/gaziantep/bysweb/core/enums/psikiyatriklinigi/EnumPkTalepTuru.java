package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 30.09.2026 08:45
 */
@Getter
public enum EnumPkTalepTuru implements BaseEnum {

    AYNI("Ayni"),
    NAKDI("Nakdi"),
    ILAC("İlaç"),
    EGITIM("Eğitim"),
    GIDA("Gıda");

    private final String label;

    EnumPkTalepTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}