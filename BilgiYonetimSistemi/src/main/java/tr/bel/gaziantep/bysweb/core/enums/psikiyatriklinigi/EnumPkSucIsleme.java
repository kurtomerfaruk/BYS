package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 14:21
 */
@Getter
public enum EnumPkSucIsleme implements BaseEnum {

    HAYIR("Hayır"),
    HIRSIZLIK("Hırsızlık"),
    UYUSTURUCU("Uyuşturucu"),
    GASP("Gasp"),
    DIGER("Diğer");

    private final String label;

    EnumPkSucIsleme(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
