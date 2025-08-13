package tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 11:54
 */
@Getter
public enum EnumShSigaraAlkolKullanimi implements BaseEnum {

    SIGARA("Sigara"),
    ALKOL("Alkol"),
    HICBIRI("Hiçbiri"),
    SIGARA_ALKOL("Sigara/Alkol");

    private final String label;

    EnumShSigaraAlkolKullanimi(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}