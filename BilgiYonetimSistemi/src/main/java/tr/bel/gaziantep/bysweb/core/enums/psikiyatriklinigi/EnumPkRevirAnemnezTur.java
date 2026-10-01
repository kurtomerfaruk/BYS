package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 10:29
 */
@Getter
public enum EnumPkRevirAnemnezTur implements BaseEnum {

    ALERJISI("Alerjisi", false,
            List.of(EnumPkRevirAnemnezTanim.ALERJISI_YOK,
                    EnumPkRevirAnemnezTanim.ALERJISI_VAR,
                    EnumPkRevirAnemnezTanim.ALERJISI_HIV,
                    EnumPkRevirAnemnezTanim.ALERJISI_ABSAG,
                    EnumPkRevirAnemnezTanim.ALERJISI_HCV)),

    ISITME("İşitme", true,
            List.of(EnumPkRevirAnemnezTanim.ISITME_KONUSABILIYOR,
                    EnumPkRevirAnemnezTanim.ISITME_KONUSAMIYOR,
                    EnumPkRevirAnemnezTanim.ISITME_DUYUYOR,
                    EnumPkRevirAnemnezTanim.ISITME_AZ_DUYUYOR,
                    EnumPkRevirAnemnezTanim.ISITME_DUYMUYOR,
                    EnumPkRevirAnemnezTanim.ISITME_CIHAZI,
                    EnumPkRevirAnemnezTanim.ISITME_DIGER)),

    GORME("Görme", true,
            List.of(EnumPkRevirAnemnezTanim.GORME_GORIYOR,
                    EnumPkRevirAnemnezTanim.GORME_GORMUYOR,
                    EnumPkRevirAnemnezTanim.GORME_KIZARIKLIK,
                    EnumPkRevirAnemnezTanim.GORME_AKINTI,
                    EnumPkRevirAnemnezTanim.GORME_SHASILIK,
                    EnumPkRevirAnemnezTanim.GORME_LENS,
                    EnumPkRevirAnemnezTanim.GORME_PROTEZ_GOZ,
                    EnumPkRevirAnemnezTanim.GORME_GOZLUK,
                    EnumPkRevirAnemnezTanim.GORME_DIGER)),

    DUYGUSAL_PSIKOLOJIK("Duygusal / Psikolojik", true,
            List.of(EnumPkRevirAnemnezTanim.DUYGUSAL_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.DUYGUSAL_SEDASYON,
                    EnumPkRevirAnemnezTanim.DUYGUSAL_ENDISELI,
                    EnumPkRevirAnemnezTanim.DUYGUSAL_AJITE,
                    EnumPkRevirAnemnezTanim.DUYGUSAL_DIGER)),

    KARDIOVASKULER("Kardiyovasküler", true,
            List.of(EnumPkRevirAnemnezTanim.KARDIOVASKULER_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_SIYANOZ,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_HIPERTANSIYON,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_GOGUS_AGRISI,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_SENKOP,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_CARPINTI,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_PRETIBIAL_ODEM,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_VARIS,
                    EnumPkRevirAnemnezTanim.KARDIOVASKULER_DIGER)),

    SOLUNUM("Solunum", true,
            List.of(EnumPkRevirAnemnezTanim.SOLUNUM_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.SOLUNUM_HEMOPTIZI,
                    EnumPkRevirAnemnezTanim.SOLUNUM_SIYANOZ,
                    EnumPkRevirAnemnezTanim.SOLUNUM_HIRLITILI,
                    EnumPkRevirAnemnezTanim.SOLUNUM_WHEEZING,
                    EnumPkRevirAnemnezTanim.SOLUNUM_BALGAM,
                    EnumPkRevirAnemnezTanim.SOLUNUM_OKSURME,
                    EnumPkRevirAnemnezTanim.SOLUNUM_GUCLUGU,
                    EnumPkRevirAnemnezTanim.SOLUNUM_YARDIMCI_ARACLAR)),

    URINER("Üriner", true,
            List.of(EnumPkRevirAnemnezTanim.URINER_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.URINER_ANURI,
                    EnumPkRevirAnemnezTanim.URINER_INKONTINANS,
                    EnumPkRevirAnemnezTanim.URINER_POLIURI,
                    EnumPkRevirAnemnezTanim.URINER_POLLAKURI,
                    EnumPkRevirAnemnezTanim.URINER_HEMATURI,
                    EnumPkRevirAnemnezTanim.URINER_DIZURI,
                    EnumPkRevirAnemnezTanim.URINER_NOKTURI,
                    EnumPkRevirAnemnezTanim.URINER_DIGER)),

    GASTROINTESTINAL("Gastrointestinal", true,
            List.of(EnumPkRevirAnemnezTanim.GASTROINTESTINAL_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_BULANTI,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_KUSMA,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_MELENA,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_AGIZ_KOKUSU,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_HEMATEMEZ,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_HEMOROID,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_ISHAL,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_KONSTIPASYON,
                    EnumPkRevirAnemnezTanim.GASTROINTESTINAL_DIGER)),

    BESLENME("Beslenme", true,
            List.of(EnumPkRevirAnemnezTanim.BESLENME_KENDISI,
                    EnumPkRevirAnemnezTanim.BESLENME_YARDIMA_GEREKSINIMI,
                    EnumPkRevirAnemnezTanim.BESLENME_YEMEK_SECIYOR)),

    ISTAH("İştah", true,
            List.of(EnumPkRevirAnemnezTanim.ISTAH_IYI,
                    EnumPkRevirAnemnezTanim.ISTAH_ORTA,
                    EnumPkRevirAnemnezTanim.ISTAH_ZAYIF,
                    EnumPkRevirAnemnezTanim.ISTAH_BULANTI_KUSMA,
                    EnumPkRevirAnemnezTanim.ISTAH_DIGER)),

    BESLENME_SEKLI("Beslenme Şekli", true,
            List.of(EnumPkRevirAnemnezTanim.BESLENME_SEKLI_ORAK,
                    EnumPkRevirAnemnezTanim.BESLENME_SEKLI_IV,
                    EnumPkRevirAnemnezTanim.BESLENME_SEKLI_NG,
                    EnumPkRevirAnemnezTanim.BESLENME_SEKLI_DIGER)),

    UYKU("Uyku", true,
            List.of(EnumPkRevirAnemnezTanim.UYKU_DUZENLI,
                    EnumPkRevirAnemnezTanim.UYKU_DUZENSIZ,
                    EnumPkRevirAnemnezTanim.UYKU_UYUMA_GUCLUGU,
                    EnumPkRevirAnemnezTanim.UYKU_UYUMASI_KOLAYLASTIRAN)),

    HAREKETLILIK("Hareketlilik", true,
            List.of(EnumPkRevirAnemnezTanim.HAREKETLILIK_SORUN_YOK,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_IMMOBIL,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_KISITLAMA,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_DENGESIZLIK,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_PARALIZI,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_FELC,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_BASTON,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_YURUTEC,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_PROTEZ,
                    EnumPkRevirAnemnezTanim.HAREKETLILIK_DEGNEK));

    private final String label;
    private final boolean cokluSecim;
    private final List<EnumPkRevirAnemnezTanim> secenekler;

    EnumPkRevirAnemnezTur(String label, boolean cokluSecim, List<EnumPkRevirAnemnezTanim> secenekler) {
        this.label = label;
        this.cokluSecim = cokluSecim;
        this.secenekler = secenekler;
    }

    public boolean isCokluSecim() {
        return cokluSecim;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
