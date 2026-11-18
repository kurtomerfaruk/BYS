package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 15:26
 */
@Getter
public enum EnumStTransferDurum implements BaseEnum {

    TASLAK("Taslak"),
    ONAYLANDI("Onaylandı"),
    SEVK_EDILDI("Sevk Edildi"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL("İptal"),
    REDDEDILDI("Reddedildi");

    private final String label;

    EnumStTransferDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}