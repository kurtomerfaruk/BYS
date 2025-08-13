package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:00
 */
@Getter
public enum EnumGnlUyruk implements BaseEnum {
    TC("Türkiye Cumhuriyeti"),
    SRY("Suriye"),
    IRAK("Irak"),
    DIGER("Diğer"),
    YABANCI("Yabancı"),
    ALMANYA("Almanya Federal Cumhuriyeti");

    private final String label;

    EnumGnlUyruk(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlUyruk fromValue(String value) throws Exception {
        for (EnumGnlUyruk uyruk : EnumGnlUyruk.values()) {
            if (uyruk.getDisplayValue().toLowerCase(Constants.LOCALE).contains(value.toLowerCase(Constants.LOCALE))) {
                return uyruk;
            }
        }
        System.out.println("EnumGnlUyruk:" + value);
        return EnumGnlUyruk.TC;
    }
}
