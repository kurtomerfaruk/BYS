package tr.bel.gaziantep.bysweb.core.enums.evdeyasamadestek;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:43
 */
@Getter
public enum EnumEdbBasvuruDurumu implements BaseEnum {

    ON_KAYIT("Ön Kayıt"),
    INCELEME_BEKLEYEN("İnceleme Bekleyen"),
    OLUMLU("Olumlu"),
    OLUMSUZ("Olumsuz"),
    IPTAL("İptal"),
    VEFAT("Vefat"),
    KIRMIZI("Kırmızı (15 Gün)"),
    YESIL("Yeşil (30 Gün)"),
    MAVI("Mavi (45 Gün)"),
    GRI("Gri (İptal / Vefat)");

    private final String label;

    EnumEdbBasvuruDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}