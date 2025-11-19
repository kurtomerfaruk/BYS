package tr.bel.gaziantep.bysweb.core.enums.moralevi;

import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.11.2025 15:12
 */
public enum EnumMeTalepDurumu implements BaseEnum {

    BEKLIYOR("Bekliyor"),
    EV_ZIYARETI_BEKLEYEN("Ev Ziyareti Bekleyen"),
    HASTANEYE_YONLENDIRILEN("Hastaneye Yönlendirilen"),
    OLUMLU("Olumlu"),
    OLUMSUZ("Olumsuz"),
    KURUMDAN_CIKIS("Kurumdan Çıkış");

    private final String label;

    EnumMeTalepDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
