package tr.bel.gaziantep.bysweb.core.enums.aktifyasam;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 16:19
 */
@Getter
public enum EnumAyDevamDurumu implements BaseEnum {

    DEVAM_EDIYOR("Devam Ediyor"),
    DEVAM_ETMIYOR("Devam Etmiyor");

    private final String modul;

    EnumAyDevamDurumu(String modul) {
        this.modul = modul;
    }

    @Override
    public String getDisplayValue() {
        return modul;
    }


}