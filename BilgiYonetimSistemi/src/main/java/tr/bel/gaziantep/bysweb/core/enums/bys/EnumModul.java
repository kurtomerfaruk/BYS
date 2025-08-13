package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:19
 */
@Getter
public enum EnumModul implements BaseEnum {

    AKTIF_YASAM("Aktif Yaşam"),
    EDB("Evim Dünyalara Bedel"),
    EKM("Engelsiz Eğitim ve Kariyer Merkezi"),
    ENGELSIZLER("Engelsizler"),
    GENEL("Genel"),
    ILERI_YAS("İleri Yaş"),
    MORAL_EVI("Moral Evi"),
    PSIKIYATRI_KLINIGI("Psikiyatri Kliniği"),
    SAGLIK_HIZMETLERI("Sağlık Hizmetleri"),
    SISTEM_YONETIMI("Sistem Yönetimi");

    private final String modul;

    EnumModul(String modul) {
        this.modul = modul;
    }

    @Override
    public String getDisplayValue() {
        return modul;
    }

    public static EnumModul fromValue(String value) throws Exception {
        for (EnumModul modul : EnumModul.values()) {
            if (modul.getDisplayValue().equals(value)) {
                return modul;
            }
        }

        throw new Exception("Tanimsiz Modul Degeri:"+value);
    }

}