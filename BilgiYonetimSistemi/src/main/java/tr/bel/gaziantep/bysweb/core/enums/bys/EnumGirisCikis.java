package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 08:46
 */
@Getter
public enum EnumGirisCikis implements BaseEnum {
    GIRIS("Giriş"),
    CIKIS("Çıkış");

    private final String label;

    EnumGirisCikis(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

