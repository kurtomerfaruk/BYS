package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.12.2025 14:17
 */
@Getter
public enum EnumGnlAramaModul implements BaseEnum {

    AKTIF_YASAM("Aktif Yaşam"),
    EDB("Evim Dünyalara Bedel"),
    EKM("EKM"),
    ENGELSIZLER("Engelsizler"),
    GENEL("Genel"),
    ILERI_YAS("İleri Yaş"),
    MORAL_EVI("Moral Evi"),
    ORTEZ_PROTEZ("Ortez - Protez"),
    SAGLIK_HIZMETLERI("Sağlık Hizmetleri");

    private final String label;

    EnumGnlAramaModul(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }


}