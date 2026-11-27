package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 15:37
 */
@Getter
public enum EnumOrtTalepTipi implements BaseEnum {

    ON_GORUSME("Ön Görüşme"),
    UCRETLI("Ücretli"),
    UCRETSIZ("Ücretsiz"),
    PROJE_KAPSAMINDA("Proje Kapsamında");

    private final String label;

    EnumOrtTalepTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

