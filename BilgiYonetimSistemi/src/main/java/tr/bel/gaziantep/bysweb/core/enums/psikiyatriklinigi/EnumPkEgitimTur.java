package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 10:52
 */
@Getter
public enum EnumPkEgitimTur implements BaseEnum {

    EGITIM("Eğitim"),
    TERAPI("Terapi");

    private final String label;

    EnumPkEgitimTur(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}