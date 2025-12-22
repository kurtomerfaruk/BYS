package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 12.12.2025 09:13
 */
@Getter
public enum EnumGnlRaporParametreTipi implements BaseEnum {

    TEXT("Metin girişi"),
    DATE("Tarih seçimi"),
    DATE_RANGE("Tarih aralığı"),
    SELECT("Açılır kutu seçim"),
    MULTI_SELECT("Çoklu seçim"),
    BOOLEAN("Evet/Hayır"),
    ENUM("Enum değer seçimi"),
    MULTI_ENUM("Çoklu enum seçimi");

    private final String label;

    EnumGnlRaporParametreTipi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
