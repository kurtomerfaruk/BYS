package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:25
 */
@Getter
public enum EnumHfAtikCinsi implements BaseEnum {

    HAFRIYAT_TOPRAGI("Hafriyat Toprağı"),
    YIKINTI_ATIGI("Yıkıntı Atığı"),
    HAFRIYAT_TOPRAGI_YIKINTI_ATIGI("Hafriyat Toprağı / Yıkıntı Atığı");

    private final String label;

    EnumHfAtikCinsi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
