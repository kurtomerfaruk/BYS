package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:02
 */
@Getter
public enum EnumGnlCinsiyet implements BaseEnum {

    ERKEK("Erkek"),
    KADIN("Kadın"),
    TANIMLANMADI("Tanımlanmadı");

    private final String label;

    EnumGnlCinsiyet(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlCinsiyet fromValue(String value) throws Exception {
        for (EnumGnlCinsiyet cinsiyet : EnumGnlCinsiyet.values()) {
            if (cinsiyet.getDisplayValue().equals(value)) {
                return cinsiyet;
            }
        }

        throw new Exception("Tanimsiz Cinsiyet Degeri :" + value);
    }

}