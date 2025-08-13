package tr.bel.gaziantep.bysweb.core.enums.engellikariyermerkezi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 11:12
 */
@Getter
public enum EnumEkmEngelOlusmaDurumu implements BaseEnum {

    DOGUSTAN("Doğuştan"),
    SONRADAN("Sonradan");

    private final String label;

    EnumEkmEngelOlusmaDurumu(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }

}