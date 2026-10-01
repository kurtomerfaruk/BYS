package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 30.09.2026 11:59
 */
@Getter
public enum EnumGnlIzinTuru implements BaseEnum {

    YILLIK("Yıllık İzin"),
    HASTALIK("Hastalık İzni"),
    IDARI("İdari İzin"),
    OLUM("Ölüm İzni");

    private final String label;

    EnumGnlIzinTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }


}
