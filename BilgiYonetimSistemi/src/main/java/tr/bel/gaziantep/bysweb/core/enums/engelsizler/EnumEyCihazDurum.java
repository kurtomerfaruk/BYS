package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 28.06.2025 08:56
 */
@Getter
public enum EnumEyCihazDurum implements BaseEnum {

    VATANDASTA("Vatandaşta"),
    GERI_ALINDI("Geri Alındı"),
    GERI_ALINAMADI("Geri Alınamadı");

    private final String label;

    EnumEyCihazDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
