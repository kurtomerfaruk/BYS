package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.09.2025 11:05
 */
@Getter
public enum EnumGnlAnketDurum implements BaseEnum {

    KAPALI("Kapalı"),
    TASLAK("Taslak"),
    YAYINDA("Yayında");

    private final String label;

    EnumGnlAnketDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
