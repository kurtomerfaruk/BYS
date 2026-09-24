package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 24.09.2026 11:37
 */
@Getter
public enum EnumPkOkulaDevamEtmemeNedeni implements BaseEnum {

    EKONOMIK_SEBEPLER("Ekonomik sebepler"),
    AILEVI_SEBEPLER("Ailevi sebepler"),
    MADDE_KULLANIMI("Madde kullanımı"),
    SAGLIK_SORUNLARI("Sağlık sorunları"),
    OKULA_UYUM_SAGLAYAMAMA("Okula uyum sağlayamama"),
    BASARISIZLIK("Başarısızlık"),
    DIGER("Diğer");

    private final String label;

    EnumPkOkulaDevamEtmemeNedeni(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
