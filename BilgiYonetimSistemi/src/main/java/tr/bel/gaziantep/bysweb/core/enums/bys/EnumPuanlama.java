package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 11:14
 */
@Getter
public enum EnumPuanlama implements BaseEnum {

    COK_IYI("Çok İyi"),
    IYI("İyi"),
    NORMAL("Normal"),
    KOTU("Kötü"),
    COK_KOTU("Çok Kötü");

    private final String label;

    EnumPuanlama(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumPuanlama fromValue(String value) {
        for (EnumPuanlama durum : EnumPuanlama.values()) {
            if (durum.getDisplayValue().equals(value)) {
                return durum;
            }
        }

        return null;
    }

}