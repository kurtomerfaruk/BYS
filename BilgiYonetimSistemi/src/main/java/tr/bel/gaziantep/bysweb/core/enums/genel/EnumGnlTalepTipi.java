package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:12
 */
@Getter
public enum EnumGnlTalepTipi implements BaseEnum {

    SAHSEN("Şahsen"),
    TELEFON("Telefon"),
    EPOSTA("E-Posta"),
    WEB_SITE("Web Sitesi"),
    ENGELLI_IZLEME("Engelli İzleme ve Değerlendirme"),
    MOBIL("Mobil"),
    GIKOM("Gikom"),
    MORAL_EVI_EV_ZIYARETI("Moral Evi Ev Ziyareti"),
    SOSYAL_MEDYA("Sosyal Medya"),
    HALK_GUNU("Halk Günü"),
    OZEL_KALEM("Özel Kalem"),
    EMNIYET_COCUK_POLISI("Emniyet Çocuk Polisi"),
    AMATEM("AMATEM"),
    MUHTARLIKLAR("Muhtarlıklar"),
    OKULLAR("Okullar"),
    BELEDIYELER("Belediyeler"),
    IRTIBAT_OFISI("İrtibat Ofisi");

    private final String label;

    EnumGnlTalepTipi(String label) {
        this.label = label;
    }


    @Override
    public String getDisplayValue() {
        return label;
    }
}
