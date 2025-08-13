package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:13
 */
@Getter
public enum EnumGnlEvinDurumu implements BaseEnum {

    MULK("Mülk"),
    KIRA("Kira"),
    LOJMAN("Lojman"),
    MIRAS("Miras"),
    DIGER("Diğer");

    private final String label;

    EnumGnlEvinDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}