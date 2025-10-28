package tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 09:20
 */
@Getter
public enum EnumEdbDurum implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi");

    private final String label;

    EnumEdbDurum(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
