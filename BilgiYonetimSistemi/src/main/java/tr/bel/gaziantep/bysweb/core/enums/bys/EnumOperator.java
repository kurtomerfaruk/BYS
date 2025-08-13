package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 08:50
 */
@Getter
public enum EnumOperator implements BaseEnum {
    ESITTIR("Eşittir"),
    KUCUKTUR("Küçüktür"),
    KUCUK_ESITTIR("Küçük Eşittir"),
    BUYUKTUR("Büyüktür"),
    BUYUK_ESITTIR("Büyük Eşittir"),
    ILE_BASLAYAN("İle Başlayan"),
    ILE_BITEN("İle Biten"),
    ICEREN("İçeren"),
    ARASINDA("Arasında"),
    ARASINDA_DEGIL("Arasında Değil");

    private final String label;

    EnumOperator(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}
