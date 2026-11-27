package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:25
 */
@Getter
public enum EnumTabRandevuDurumu implements BaseEnum {

    PLANLANDI("Planlandı"),
    GERCEKLESTI("Gerçekleşti"),
    ERTELENDI("Ertelendi"),
    IPTAL("İptal");

    private final String label;

    EnumTabRandevuDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
