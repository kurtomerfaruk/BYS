package tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:46
 */
@Getter
public enum EnumEdbOzelDurum implements BaseEnum {
    TUTUKLU("Tutuklu"),
    ASKER("Asker"),
    HUKUMLU("Hükümlü"),
    GAZI("Gazi"),
    SEHIT("Şehit");

    private final String label;

    EnumEdbOzelDurum(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumEdbOzelDurum fromValue(String value) {
        for (EnumEdbOzelDurum durum : EnumEdbOzelDurum.values()) {
            if (durum.getDisplayValue().equals(value)) {
                return durum;
            }
        }

       return null;
    }

}
