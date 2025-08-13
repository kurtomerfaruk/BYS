package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:11
 */
@Getter
public enum EnumGnlSosyalGuvence implements BaseEnum {

    YOK("Yok"),
    SSK("Ssk"),
    BAGKUR("Bağkur"),
    EMEKLI_SANDIGI("Emekli Sandığı"),
    YESIL_KART("Yeşil Kart"),
    OZEL_SIGORTA("Özel Sigorta");

    private final String label;

    EnumGnlSosyalGuvence(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}