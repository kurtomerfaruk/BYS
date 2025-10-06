package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 10:53
 */
@Getter
public enum EnumOrtBasvuruHareketDurumu implements BaseEnum {
    OLUMLU("Olumlu"),
    OLCU_ICIN_RANDEVU_VERILDI("Ölçü için randevu verildi"),
    OLCU_ALINDI("Ölçü alındı"),
    OLCU_SONRASI_RANDEVU_VERILDI("Ölçü sonrası randevu verildi");

    private final String label;

    EnumOrtBasvuruHareketDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}