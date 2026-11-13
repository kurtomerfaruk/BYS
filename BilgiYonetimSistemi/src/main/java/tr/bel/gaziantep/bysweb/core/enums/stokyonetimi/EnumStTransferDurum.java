package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStTransferDurum implements BaseEnum {
    TASLAK("Taslak"),
    ONAY_BEKLIYOR("Onay Bekliyor"),
    SEVKEDILDI("Sevk Edildi"),
    KISMEN_TESLIM("Kısmen Teslim"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL("İptal");

    private final String label;

    EnumStTransferDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
