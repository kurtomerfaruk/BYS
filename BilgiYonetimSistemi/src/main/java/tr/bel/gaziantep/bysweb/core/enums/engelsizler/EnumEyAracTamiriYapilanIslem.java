package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 13:43
 */
@Getter
public enum EnumEyAracTamiriYapilanIslem implements BaseEnum {

    AKU("Akü"),
    DENGE_MAKASI("Denge Makası"),
    GENEL_BAKIM("Genel Bakım"),
    JOYSTICK("Joystick"),
    KOLCAK("Kolçak"),
    KOMUR_DEGISIMI("Kömür Değişimi"),
    MOTOR_BAKIMI("Motor Bakımı"),
    ON_TEKER_MASASI("Ön Teker Maşası"),
    ON_TEKERLEK_BILYESI("Ön Tekerlek Bilyesi"),
    TEKERLEK_ARKA("Tekerlek Arka"),
    TEKERLEK_ON("Tekerlek Ön"),
    YAG_DEGISIMI("Yağ Değişimi");


    private final String label;

    EnumEyAracTamiriYapilanIslem(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}
