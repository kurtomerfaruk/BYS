package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:28
 */
@Getter
public enum EnumGnlBelediye implements BaseEnum {

    ARABAN("Araban"),
    ISLAHIYE("İslahiye"),
    KARKAMIS("Karkamış"),
    NIZIP("Nizip"),
    NURDAGI("Nurdağı"),
    OGUZELI("Oğuzeli"),
    SAHINBEY("Şahinbey"),
    SEHITKAMIL("Şehitkamil"),
    YAVUZELI("Yavuzeli");

    private final String label;

    EnumGnlBelediye(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
