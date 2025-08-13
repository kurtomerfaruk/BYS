package tr.bel.gaziantep.bysweb.core.enums.hafriyat;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:43
 */
@Getter
public enum EnumHfMalCinsi implements BaseEnum {

    HAFRIYAT("Hafriyat"),
    INSAAT_ATIGI("İnşaat Atığı"),
    HURDA_CAM("Hurda Cam"),
    KOMUR_KULU("Kömür Külü"),
    MICIR("Mıcır"),
    MICIR_05("Mıcır 0-5"),
    MICIR_032("Mıcır 0-32"),
    MICIR_038("Mıcır 0-38"),
    MICIR_050("Mıcır 0-50"),
    MICIR_070("Mıcır 0-70"),;

    private final String label;

    EnumHfMalCinsi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}