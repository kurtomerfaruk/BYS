package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 14:37
 */
@Getter
public enum EnumPkBeden implements BaseEnum {

    SAG_KOL("Sağ kol"),
    SOL_KOL("Sol Kol"),
    GOGUS("Göğüs"),
    BACAK("Bacak");

    private final String label;

    EnumPkBeden(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
