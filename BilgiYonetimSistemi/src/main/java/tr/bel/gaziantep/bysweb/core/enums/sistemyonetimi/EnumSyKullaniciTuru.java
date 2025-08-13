package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 09:20
 */
@Getter
public enum EnumSyKullaniciTuru implements BaseEnum {

    SISTEM_YONETICISI("Sistem Yöneticisi"),
    KULLANICI("Kullanıcı");

    private final String label;

    EnumSyKullaniciTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}