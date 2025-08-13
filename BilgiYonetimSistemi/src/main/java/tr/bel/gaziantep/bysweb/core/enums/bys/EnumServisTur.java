package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:53
 */
@Getter
public enum EnumServisTur implements BaseEnum {

    GAZIKART("GaziKart");

    private final String modul;

    EnumServisTur(String modul) {
        this.modul = modul;
    }

    @Override
    public String getDisplayValue() {
        return modul;
    }
}
