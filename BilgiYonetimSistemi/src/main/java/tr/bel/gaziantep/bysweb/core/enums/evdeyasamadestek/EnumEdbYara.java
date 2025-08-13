package tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 10:52
 */
@Getter
public enum EnumEdbYara implements BaseEnum {
    BASINC_YARALARI("Basınç Yaraları"),
    DIYABETIK_RAPORLAR("Diyabetik Raporlar"),
    TRAVMATIK_YARALAR("Travmatik Yaralar"),
    CERRAHI_YARALAR("Cerrahi Yaralar"),
    YARA_YOK("Yara Yok");

    private final String label;

    EnumEdbYara(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
