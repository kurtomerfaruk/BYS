package tr.bel.gaziantep.bysweb.core.enums.stokyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 11:37
 */
@Getter
public enum EnumStHareketTuru implements BaseEnum {

    SATIN_ALMA("Satın Alma", EnumGirisCikis.GIRIS),
    SATIS("Satış", EnumGirisCikis.CIKIS),
    TRANSFER_GIRIS("Transfer Girişi", EnumGirisCikis.GIRIS),
    TRANSFER_CIKIS("Transfer Çıkışı", EnumGirisCikis.CIKIS),
    SAYIM_FARKI("Sayım Farkı", null),
    IADE_GIRIS("İade Girişi", EnumGirisCikis.GIRIS),
    IADE_CIKIS("İade Çıkışı", EnumGirisCikis.CIKIS),
    FIRE("Fire", EnumGirisCikis.CIKIS),
    ZAYI("Zayi", EnumGirisCikis.CIKIS),
    SARF("Sarf", EnumGirisCikis.CIKIS),
    ACILIS("Açılış", EnumGirisCikis.GIRIS),
    URUN_CIKISI("Ürün Çıkışı", EnumGirisCikis.CIKIS),
    URETIM_GIRISI("Üretim Girişi", EnumGirisCikis.GIRIS),
    MANUEL_DUZELTME("Manuel Düzeltme", null);

    private final String label;
    private final EnumGirisCikis varsayilanYon;

    EnumStHareketTuru(String label, EnumGirisCikis varsayilanYon) {
        this.label = label;
        this.varsayilanYon = varsayilanYon;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}

