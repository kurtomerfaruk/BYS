package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 10:30
 */
@Getter
public enum EnumStSayimDurum implements BaseEnum {

    TASLAK("Taslak"),
    SAYIMDA("Sayımda"),
    ONAY_BEKLIYOR("Onay Bekliyor"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL("İptal");

    private final String label;

    EnumStSayimDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
