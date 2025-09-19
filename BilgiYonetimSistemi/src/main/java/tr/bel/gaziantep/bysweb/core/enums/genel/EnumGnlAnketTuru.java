package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.09.2025 11:07
 */
@Getter
public enum EnumGnlAnketTuru implements BaseEnum {

    KULLANICI("Kullanıcı"),
    KISI("Kişi"),
    GENEL("Genel");

    private final String label;

    EnumGnlAnketTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
