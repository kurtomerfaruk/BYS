package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 10:30
 */
@Getter
public enum EnumHfTahsilatTuru implements BaseEnum {

    AVANS("Avans"),
    BANKA_TAHSILAT("Banka Tahsilat"),
    CEK("Çek"),
    EFT("EFT"),
    HAVALE("Havale"),
    NAKIT("Nakit"),
    KREDI_KARTI("Kredi Kartı");

    private final String label;

    EnumHfTahsilatTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}