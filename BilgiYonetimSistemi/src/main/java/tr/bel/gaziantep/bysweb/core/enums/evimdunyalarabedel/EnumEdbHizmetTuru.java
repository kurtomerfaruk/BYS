package tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:51
 */
@Getter
public enum EnumEdbHizmetTuru implements BaseEnum {

    KISISEL_BAKIM("Kişisel Bakım"),
    TEMIZLIK("Temizlik"),
    TADILAT_ONARIM("Tadilat-Onarım"),
    PSIKOLOJIK_DESTEK("Psikolojik Destek"),
    EVDE_SAGLIK_HIZMETI("Evde Sağlık Hizmeti"),
    ASANSORLU_ARAC_DESTEGI("Asansörlü Araç Desteği"),
    MEDIKAL_MALZEME_DESTEGI("Medikal Malzeme Desteği"),
    AYNI_YARDIM_DESTEGI("Ayni Yardım Desteği");

    private final String label;

    EnumEdbHizmetTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumEdbHizmetTuru fromValue(String value){
        for (EnumEdbHizmetTuru hizmet : EnumEdbHizmetTuru.values()) {
            if(hizmet.name().equals(value)){
                return hizmet;
            }
        }

        return null;
    }

}