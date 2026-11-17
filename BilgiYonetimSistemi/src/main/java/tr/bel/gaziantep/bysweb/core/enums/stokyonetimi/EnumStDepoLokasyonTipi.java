package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:20
 */
@Getter
public enum EnumStDepoLokasyonTipi implements BaseEnum {

    BOLGE("Bölge"),
    KORIDOR("Koridor"),
    RAF("Raf"),
    GOZ("Göz"),
    DIGER("Diğer");

    private final String label;

    EnumStDepoLokasyonTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}