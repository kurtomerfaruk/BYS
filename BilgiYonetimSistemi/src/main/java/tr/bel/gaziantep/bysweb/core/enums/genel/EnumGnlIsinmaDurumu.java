package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:14
 */
@Getter
public enum EnumGnlIsinmaDurumu implements BaseEnum {

    KALORIFER("Kalorifer"),
    SOBA("Soba"),
    DOGALGAZ("Doğalgaz"),
    ELEKTRIK("Elektrik"),
    YOK("Yok");

    private final String label;

    EnumGnlIsinmaDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}