package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStYetkiTuru implements BaseEnum {
    GORUNTULE("Görüntüle"),
    ISLEM("İşlem"),
    ONAY("Onay"),
    RAPOR("Rapor");

    private final String label;

    EnumStYetkiTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
