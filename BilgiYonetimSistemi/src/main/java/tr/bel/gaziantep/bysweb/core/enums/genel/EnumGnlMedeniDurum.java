package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:04
 */
@Getter
public enum EnumGnlMedeniDurum implements BaseEnum {

    BEKAR("Bekâr"),
    EVLI("Evli"),
    BOSANMIS("Boşanmış"),
    DUL("Dul"),
    EVLILIGIN_IPTALI("Evliliğin İptali"),
    NIKAHSIZ("Nikahsız"),
    TERKEDILMIS("Terkedilmiş"),
    OKSUZ_YETIM("Öksüz/Yetim"),
    TANIMLANMADI("Tanımlanmadı");

    private final String label;

    EnumGnlMedeniDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlMedeniDurum fromValue(String value) throws Exception {
        for (EnumGnlMedeniDurum hal : EnumGnlMedeniDurum.values()) {
            if (hal.getDisplayValue().equals(value)) {
                return hal;
            }
        }

        throw new Exception("Tanimsiz Medeni Durum degeri :"+value);
    }

}
