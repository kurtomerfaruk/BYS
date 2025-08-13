package tr.bel.gaziantep.bysweb.core.enums.genel;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 14:06
 */
@Getter
public enum EnumGnlEgitimDurumu implements BaseEnum {
    ILKOGRETIM("İlköğretim"),
    ONLISANS("2 Yıllık Önlisans"),
    ORTAOKUL_MESLEKI_ORTAOKUL("Ortaokul veya Mesleki Ortaokul"),
    LISE_VE_DENGI_OKULLAR("Lise ve Dengi Okullar"),
    LISANS("4 Yıllık Lisans"),
    YUKSEK_LISANS_DOKTORA("Yüksek Lisans-Doktora"),
    HICBIRI("Hiçbiri"),
    OKUR_YAZAR("Okur-Yazar"),
    COCUK_BEBEK("Çocuk/Bebek"),
    OKUR_YAZAR_DEGIL("Okur-Yazar Değil"),
    OZEL_EGITIM("Özel Eğitim"),
    ILKOGRETIM_MEZUNU("İlk Öğretim Mezunu"),
    ORTAOGRETIM_MEZUNU("Orta Öğretim Mezunu"),
    ILKOKUL_TERK("İlkokul Terk"),
    ORTAOKUL_TERK("Ortaokul Terk"),
    LISE_TERK("Lise Terk"),
    ACIKOGRETIME_KAYITLI("Açıköğretime Kayıtlı"),
    TANIMLANMADI("Tanımlanmadı");

    private final String label;

    EnumGnlEgitimDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

    public static EnumGnlEgitimDurumu fromValue(String value) throws Exception {
        for (EnumGnlEgitimDurumu durum : EnumGnlEgitimDurumu.values()) {
            if (durum.getDisplayValue().equals(value)) {
                return durum;
            }
        }

        throw new Exception("Tanimsiz Egitim Durumu degeri :"+value);
    }

}
