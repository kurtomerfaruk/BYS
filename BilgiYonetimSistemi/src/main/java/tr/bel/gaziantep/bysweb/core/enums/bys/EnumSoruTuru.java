package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 15:27
 */
@Getter
public enum EnumSoruTuru implements BaseEnum {
    TEK_SECIMLI("Tekli Seçim"),
    SAYI("Sayı"),
    COKTAN_SECMELI("Çoktan Seçmeli");

    private final String label;

    EnumSoruTuru(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
