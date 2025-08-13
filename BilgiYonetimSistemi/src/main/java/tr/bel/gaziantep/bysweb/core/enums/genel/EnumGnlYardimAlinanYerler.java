package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:31
 */
@Getter
public enum EnumGnlYardimAlinanYerler implements BaseEnum {

    AKRABADAN_ARKADASTAN("Akrabadan-Arkadaştan"),
    VAKIF("Vakıf"),
    DERNEK("Dernek"),
    BELEDIYEDEN("Belediyeden"),
    KAYMAKAMLIKTAN("Kaymakamlıktan"),
    ASP("ASP İl Müdürlüğü"),
    CEVRE("Çevre"),
    DIGER("Diğer"),
    ALMIYOR("Almıyor");

    private final String label;

    EnumGnlYardimAlinanYerler(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
