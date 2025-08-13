package tr.bel.gaziantep.bysweb.core.enums.saglikhizmetleri;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 16:29
 */
@Getter
public enum  EnumShDanismanlikHizmeti implements BaseEnum {

    DOKTOR("Doktor"),
    DIYETISYEN("Diyetisyen"),
    HEMSIRE_EBE("Hemşire / Ebe"),
    OBEZITE_KAMPI("Obezite Kampı"),
    PSIKOLOJIK_DANISMANLIK("Psikolojik Danışmanlık");

    private final String label;

    EnumShDanismanlikHizmeti(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}

