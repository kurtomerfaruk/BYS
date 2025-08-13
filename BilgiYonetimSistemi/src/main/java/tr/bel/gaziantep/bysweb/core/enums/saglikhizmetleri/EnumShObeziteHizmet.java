package tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 16:29
 */
@Getter
public enum EnumShObeziteHizmet implements BaseEnum {

    BESLENME_PLANI("Beslenme Planı ve Danışmanlık"),
    GENEL_BESLENME("Genel Beslenme Danışmanlığı"),
    FIZIKSEL_AKTIVITE("Fiziksel Aktivite/Egzersiz Danışmanlığı");

    private final String label;

    EnumShObeziteHizmet(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
