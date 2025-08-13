package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:13
 */
@Getter
public enum EnumGnlTalepDurumu implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    TAMAMLANDI("Tamamlandı"),
    TAMAMLANMADI("Tamamlanmadı");

    private final String label;

    EnumGnlTalepDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
