package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.12.2025 09:52
 */
@Getter
public enum EnumGnlRaporGrupTuru implements BaseEnum {

    SUM("Sum"),
    AVG("Avg"),
    COUNT("Count"),
    MIN("Min"),
    MAX("Max");

    private final String label;

    EnumGnlRaporGrupTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}