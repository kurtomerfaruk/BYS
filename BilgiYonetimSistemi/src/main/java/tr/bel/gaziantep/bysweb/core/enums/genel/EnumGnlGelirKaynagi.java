package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:28
 */
@Getter
public enum EnumGnlGelirKaynagi implements BaseEnum {

    ENGELLI_MAASI("2022 Maaşı"),
    EVDE_BAKIM_MAASI("Evde Bakım Maaşı"),
    CALISAN("Çalışan"),
    EMEKLI("Emekli"),
    GAZI("Gazi"),
    MEMUR("Memur"),
    ALTMISBES_YAS_USTU("65 Yaş Üstü Yaşlılık Aylığı"),
    DIGER("Diğer");

    private final String label;

    EnumGnlGelirKaynagi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}
