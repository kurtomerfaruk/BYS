package tr.bel.gaziantep.bysweb.core.enums.engelsizler;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:52
 */
@Getter
public enum EnumEyKullandigiCihaz implements BaseEnum {

    AKULU_TEKERLEKLI_SANDALYE("Akülü Tekerlekli Sandalye"),
    MANUEL_TEKERLEKLI_SANDALYE("Manuel Tekerlekli Sandalye"),
    ORTEZ_PROTEZ("Ortez,Protez"),
    KOLTUK_DEGNEGI("Koltuk Değneği"),
    BEYAZ_BASTON("Beyaz Baston"),
    HASTA_YATAGI("Hasta Yatağı"),
    SISME_YATAK("Şişme Yatak"),
    WALKER("Walker"),
    SEYYAR_WC("Seyyar WC"),
    DEVREK_BASTONU("Devrek Bastonu"),
    SOLUNUM_DESTEK_CIHAZI("Solunum Destek Cihazı"),
    SONDA("Sonda"),
    DIREN("Diren"),
    DIGER("Diğer(Açıklama)");

    private final String label;

    EnumEyKullandigiCihaz(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
