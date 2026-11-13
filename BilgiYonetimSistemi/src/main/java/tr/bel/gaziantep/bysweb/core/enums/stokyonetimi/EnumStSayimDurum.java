package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 13.11.2026 09:49
 */
@Getter
public enum EnumStSayimDurum implements BaseEnum {
    PLANLI("Planlı"),
    DEVAM("Devam Ediyor"),
    TAMAMLANDI("Tamamlandı"),
    ONAYLANDI("Onaylandı"),
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
