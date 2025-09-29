package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:07
 */
@Getter
public enum EnumOrtEngelOlusum implements BaseEnum {

    DEPREM_ONCESI("Deprem Öncesi"),
    DEPREM_SONRASI("Deprem Sonrası");

    private final String label;

    EnumOrtEngelOlusum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}