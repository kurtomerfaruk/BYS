package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 17.09.2025 11:17
 */
@Getter
public enum EnumGnlAnketSoruTuru implements BaseEnum {

    TEXT("Metin"),
    SELECT_ONE("Tekli Seçim"),
    SELECT_MANY("Çoklu Seçim"),
    DATE("Tarih"),
    NUMBER("Sayı");

    private final String label;

    EnumGnlAnketSoruTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}