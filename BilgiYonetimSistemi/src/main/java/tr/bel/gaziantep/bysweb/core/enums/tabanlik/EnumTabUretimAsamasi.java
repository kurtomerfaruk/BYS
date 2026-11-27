package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Getter
public enum EnumTabUretimAsamasi implements BaseEnum {

    URETIM_BEKLIYOR("Üretim Bekliyor"),
    URETIMDE("Üretimde"),
    URETIM_TAMAMLANDI("Üretim Tamamlandı");

    private final String label;

    EnumTabUretimAsamasi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
