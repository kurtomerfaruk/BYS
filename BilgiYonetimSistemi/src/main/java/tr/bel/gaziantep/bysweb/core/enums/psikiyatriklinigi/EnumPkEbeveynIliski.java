package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 28.09.2026 10:01
 */
@Getter
public enum EnumPkEbeveynIliski implements BaseEnum {

    IYI("İyi"),
    COK_IYI("Çok İyi"),
    KOTU("Kötü"),
    BOSANMIS("Boşanmış"),
    AYRI_YASIYORLAR("Ayrı yaşıyorlar"),
    IMAM_NIKAHLI("İmam Nikahlı"),
    DIGER("Diğer");

    private final String label;

    EnumPkEbeveynIliski(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}