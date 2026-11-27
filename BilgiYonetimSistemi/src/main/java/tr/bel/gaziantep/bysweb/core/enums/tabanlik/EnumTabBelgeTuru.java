package tr.bel.gaziantep.bysweb.core.enums.tabanlik;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 13:57
 */
@Getter
public enum EnumTabBelgeTuru implements BaseEnum {

    OLCU_FORMU("Ölçüm Formu"),
    SOZLESME("Sözleşme"),
    DEKONT("Dekont"),
    UCRETSIZ_ONAY("Ücretsiz İşlem Onayı"),
    TESLIMAT_FORMU("Teslimat Formu"),
    DIGER("Diğer");

    private final String label;

    EnumTabBelgeTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}