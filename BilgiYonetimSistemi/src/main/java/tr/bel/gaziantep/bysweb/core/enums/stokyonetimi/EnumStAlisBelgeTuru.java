package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 16:01
 */
@Getter
public enum EnumStAlisBelgeTuru implements BaseEnum {

    BAGIS("Bağış"),
    IHALE("İhale"),
    DOGRUDAN_TEMIN("Doğrudan Temin");

    private final String label;

    EnumStAlisBelgeTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
