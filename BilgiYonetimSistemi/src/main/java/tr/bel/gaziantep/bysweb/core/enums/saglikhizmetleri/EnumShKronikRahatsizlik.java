package tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 11:56
 */
@Getter
public enum EnumShKronikRahatsizlik implements BaseEnum {
    ASTIM("Astım"),
    DIYABET("Diyabet"),
    HIPER_TANSIYON("Hiper Tansiyon"),
    KANSER("Kanser"),
    KOAH("KOAH"),
    KORONER_KALP_HASTALIGI("Koroner Kalp Hastalığı"),
    TROID_FONKSIYOLARI_BOZUK("Troid Fonksiyonları Bozuk"),
    DIGER("Diğer"),
    YOK("Yok");

    private final String label;

    EnumShKronikRahatsizlik(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
