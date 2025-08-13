package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 13:38
 */
@Getter
public enum EnumEyArizaTuru implements BaseEnum {

    ELEKTRIK("Elektrik"),
    MEKANIK("Mekanik"),
    YEDEK_PARCALI("Yedek Parçalı"),
    YEDEK_PARCASIZ("Yedek Parçasız");


    private final String label;

    EnumEyArizaTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumEyArizaTuru fromValue(String value) {
        for (EnumEyArizaTuru arizaTuru : EnumEyArizaTuru.values()) {
            if (arizaTuru.name().equals(value.trim())) {
                return arizaTuru;
            }
        }
        return null;
    }

}
