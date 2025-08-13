package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.07.2025 16:29
 */
@Getter
public enum EnumHfFirmaTuru implements BaseEnum {

    URETICI("Üretici"),
    TASIYICI("Taşıyıcı"),
    DEPOLAMA_TESISI("Depolama Tesisi"),
    URETICI_TASIYICI("Üretici/Taşıyıcı");

    private final String label;

    EnumHfFirmaTuru(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
