package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 10:20
 */
@Getter
public enum EnumEyAnketDurumu implements BaseEnum {

    ANKET_YAPILDI("Anket Yapıldı"),
    ANKET_YAPILMADI("Anket Yapılmadı"),
    EVDE_YOK("Evde Yok"),
    RED("Red"),
    TELEFON_ILE_ANKET_YAPILDI("Telefon İle Anket Yapıldı");

    private final String label;

    EnumEyAnketDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}