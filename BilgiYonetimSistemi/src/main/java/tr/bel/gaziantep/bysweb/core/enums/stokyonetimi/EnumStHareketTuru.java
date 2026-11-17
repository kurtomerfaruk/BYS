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

    SATIN_ALMA("Satýn Alma", EnumGirisCikis.GIRIS),
    SATIS("Satýþ", EnumGirisCikis.CIKIS),
    TRANSFER_GIRIS("Transfer Giriþi", EnumGirisCikis.GIRIS),
    TRANSFER_CIKIS("Transfer Çýkýþý", EnumGirisCikis.CIKIS),
    SAYIM_FARKI("Sayým Farký", null),
    IADE_GIRIS("Ýade Giriþi", EnumGirisCikis.GIRIS),
    IADE_CIKIS("Ýade Çýkýþý", EnumGirisCikis.CIKIS),
    FIRE("Fire", EnumGirisCikis.CIKIS),
    ZAYI("Zayi", EnumGirisCikis.CIKIS),
    SARF("Sarf", EnumGirisCikis.CIKIS),
    URETIM_GIRIS("Üretim Giriþi", EnumGirisCikis.GIRIS),
    URETIM_CIKIS("Üretim Çýkýþý", EnumGirisCikis.CIKIS),
    ACILIS("Açýlýþ", EnumGirisCikis.GIRIS),
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

