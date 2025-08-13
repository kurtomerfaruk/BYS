package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:11
 */
@Getter
public enum EnumGnlTalepTuru implements BaseEnum {

    TALEP("Talep"),
    SIKAYET("Şikayet"),
    ONERI("Öneri"),
    KURUMDAN_AYRILIS("Kurumdan Ayrılış");

    private final String label;

    EnumGnlTalepTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
