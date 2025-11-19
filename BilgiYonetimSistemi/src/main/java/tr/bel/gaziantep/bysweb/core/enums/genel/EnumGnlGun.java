package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.11.2025 11:29
 */
@Getter
public enum EnumGnlGun implements BaseEnum {

    PAZARTESI("Pazartesi"),
    SALI("Salı"),
    CARSAMBA("Çarşamba"),
    PERSEMBE("Perşembe"),
    CUMA("Cuma"),
    CUMARTESI("Cumartesi"),
    PAZAR("Pazar");

    private final String label;

    EnumGnlGun(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }


}