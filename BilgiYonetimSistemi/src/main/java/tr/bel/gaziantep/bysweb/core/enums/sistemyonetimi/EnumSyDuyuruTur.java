package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 08:57
 */
@Getter
public enum EnumSyDuyuruTur implements BaseEnum {

    GENEL("Genel"),
    KULLANICILAR("Kullanıcılar");

    private final String label;

    EnumSyDuyuruTur(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
