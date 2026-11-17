package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:20
 */
@Getter
public enum EnumStMaliyetYontemi implements BaseEnum {

    FIFO("FIFO"),
    LIFO("LIFO"),
    WEIGHTED_AVERAGE("Agirlikli Ortalama");

    private final String label;

    EnumStMaliyetYontemi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
