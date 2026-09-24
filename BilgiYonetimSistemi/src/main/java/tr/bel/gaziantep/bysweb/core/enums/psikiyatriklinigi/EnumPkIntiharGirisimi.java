package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 14:18
 */
@Getter
public enum EnumPkIntiharGirisimi implements BaseEnum {

    YOK("Yok"),
    MADDELIYKEN("Maddeliyken"),
    AYIKKEN("Ayıkken"),
    YONTEMI("Yöntemi");

    private final String label;

    EnumPkIntiharGirisimi(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
