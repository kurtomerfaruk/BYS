package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:14
 */
@Getter
public enum EnumEyMaddeKullanimi implements BaseEnum {

    SIGARA("Sigara"),
    ALKOL("Alkol"),
    MADDE_BAGIMLILIGI("Madde Bağımlılığı");

    private final String label;

    EnumEyMaddeKullanimi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}