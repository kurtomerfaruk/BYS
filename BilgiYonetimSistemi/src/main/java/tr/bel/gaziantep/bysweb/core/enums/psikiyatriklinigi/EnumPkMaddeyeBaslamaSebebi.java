package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 13:39
 */
@Getter
public enum EnumPkMaddeyeBaslamaSebebi implements BaseEnum {

    MERAK("Merak"),
    SOSYAL_CEVRE("Sosyal çevre"),
    SOSYAL_SORUNLAR("Sosyal sorunlar"),
    AILEVI_SORUNLAR("Ailevi sorunlar"),
    ARKADAS_CEVRESI("Arkadaş çevresi"),
    PSIKIYATRIK_SORUNLAR("Psikiyatrik sorunlar"),
    SAGLIK_SORUNLARI("Sağlık sorunları"),
    DIGER("Diğer");

    private final String label;

    EnumPkMaddeyeBaslamaSebebi(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}