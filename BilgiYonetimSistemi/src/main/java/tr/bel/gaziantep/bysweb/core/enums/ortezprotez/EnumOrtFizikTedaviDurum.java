package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.10.2025 15:555
 */
@Getter
public enum EnumOrtFizikTedaviDurum implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    TAMAMLANDI("Tamamlandı"),
    IPTAL_EDILDI("İptal Edildi");

    private final String label;

    EnumOrtFizikTedaviDurum(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
