package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 13:20
 */
@Getter
public enum EnumStIslemLogTuru implements BaseEnum {

    HAREKET("Stok Hareketi"),
    TRANSFER("Transfer"),
    SAYIM("Sayım"),
    YETKI("Yetki Değişikliği"),
    GENEL("Genel");

    private final String label;

    EnumStIslemLogTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

