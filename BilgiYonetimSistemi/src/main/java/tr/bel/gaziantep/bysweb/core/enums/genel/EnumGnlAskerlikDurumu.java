package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:20
 */
@Getter
public enum EnumGnlAskerlikDurumu implements BaseEnum {
    TAMAMLANDI("Tamamlandı"),
    MUAF("Muaf"),
    TECILLI("Tecilli");

    private final String label;

    EnumGnlAskerlikDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}