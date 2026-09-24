package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 12:53
 */
@Getter
public enum EnumPkNeredeKimlerleYasiyor implements BaseEnum {

    ANNE_BABA_ILE("Anneyle-Babayla"),
    ANNE_ILE("Anne ile"),
    BABA_ILE("Baba ile"),
    GENIS_AILE("Geniş aile ile"),
    DIGER("Diğer");

    private final String label;

    EnumPkNeredeKimlerleYasiyor(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
