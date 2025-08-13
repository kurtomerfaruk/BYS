package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:16
 */
@Getter
public enum EnumGnlCalismaDurumu implements BaseEnum {

    CALISIYOR("Çalışıyor"),
    CALISMIYOR("Çalışmıyor"),
    EMEKLI("Emekli"),
    EMEKLI_CALISIYOR("Emekli+Çalışıyor"),
    TANIMLANMADI("Tanımlanmadı");

    private final String label;

    EnumGnlCalismaDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}