package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:09
 */
@Getter
public enum EnumGnlDurum implements BaseEnum {

    SAG("Sağ"),
    OLU("Ölü"),
    OLUM("Ölüm"),
    TANIMLANMADI("Tanımlanmadı"),
    VATANDASLIKTAN_CIKMA("Vatandaşlıktan Çıkma"),
    IKAMET_SURESI_BITTI("İkamet Süresi Bitti"),
    OLUMUN_TESPITI("Ölümün Tespiti"),
    KAPALI("Kapalı");

    private final String label;

    EnumGnlDurum(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlDurum fromValue(String value) throws Exception {
        if(value.equals("Açık")){
            return EnumGnlDurum.SAG;
        }
        for (EnumGnlDurum durum : EnumGnlDurum.values()) {
            if(durum.getDisplayValue().equals(value)){
                return durum;
            }
        }

        throw new Exception("Tanimsiz Durum Degeri : "+value);
    }

}
