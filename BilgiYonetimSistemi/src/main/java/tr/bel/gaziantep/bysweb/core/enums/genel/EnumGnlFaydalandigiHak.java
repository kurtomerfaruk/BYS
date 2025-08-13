package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:09
 */
@Getter
public enum EnumGnlFaydalandigiHak implements BaseEnum {

    ENGELLI_KIMLIK_KARTI("Engelli Kimlik Kartı"),
    ENGELLI_ULASIM_KARTI("Engelli Ulaşım Kartı"),
    ENGELLI_MAASI("2022 Maaşı"),
    EVDE_BAKIM_MAASI("Evde Bakım Maaşı"),
    SU_INDIRIMI("Su İndirimi"),
    OTV_INDIRIMI("ÖTV İndirimi"),
    VERGI_INDIRIMI("Vergi İndirimi"),
    ELEKTRIK("Elektrik"),
    DIGER("Diğer"),
    CALISAN("Çalışan"),
    EMEKLI("Emekli"),
    GAZI("Gazi"),
    MEMUR("Memur"),
    ALTMISBES_YAS_AYLIGI("65 Yaş Aylığı");

    private final String label;

    EnumGnlFaydalandigiHak(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlFaydalandigiHak fromValue(String value) throws Exception {
        for (EnumGnlFaydalandigiHak hak : EnumGnlFaydalandigiHak.values()) {
            if (hak.toString().equals(value)) {
                return hak;
            }
        }

        throw new Exception("Tanimsiz Faydalandigi Hak Degeri :" + value);
    }

    public static EnumGnlFaydalandigiHak fromValueDisplay(String value) throws Exception {
        for (EnumGnlFaydalandigiHak hak : EnumGnlFaydalandigiHak.values()) {
            if (hak.getDisplayValue().equals(value)) {
                return hak;
            }
        }

        throw new Exception("Tanimsiz Faydalandigi Hak Degeri :" + value);
    }

}