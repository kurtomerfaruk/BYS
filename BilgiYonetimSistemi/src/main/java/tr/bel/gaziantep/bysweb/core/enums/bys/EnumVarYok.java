package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 12:15
 */
@Getter
public enum EnumVarYok implements BaseEnum {
    VAR("Var"),
    YOK("Yok");

    private final String label;

    EnumVarYok(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}

