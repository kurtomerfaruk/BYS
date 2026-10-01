package tr.bel.gaziantep.bysweb.core.enums.psikiyatriklinigi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 1.10.2026 10:28
 */
@Getter
public enum EnumPkRevirAnemnezTanim implements BaseEnum {

    // 4.1.10 ALERJISI (tek secim)
    ALERJISI_YOK("Yok"),
    ALERJISI_VAR("Var"),
    ALERJISI_HIV("HIV"),
    ALERJISI_ABSAG("AbsAg"),
    ALERJISI_HCV("HCV"),

    // 4.1.14 ISITME
    ISITME_KONUSABILIYOR("Konuşabiliyor"),
    ISITME_KONUSAMIYOR("Konuşamıyor"),
    ISITME_DUYUYOR("Duyuyor"),
    ISITME_AZ_DUYUYOR("Az Duyuyor"),
    ISITME_DUYMUYOR("Duymuyor"),
    ISITME_CIHAZI("İşitme cihazı"),
    ISITME_DIGER("Diğer", true),

    // 4.1.15 GORME
    GORME_GORIYOR("Görüyor"),
    GORME_GORMUYOR("Görmüyor"),
    GORME_KIZARIKLIK("Kızarıklık"),
    GORME_AKINTI("Akıntı"),
    GORME_SHASILIK("Şaşılık"),
    GORME_LENS("Lens"),
    GORME_PROTEZ_GOZ("Protez göz"),
    GORME_GOZLUK("Gözlük"),
    GORME_DIGER("Diğer", true),

    // 4.1.16 DUYGUSAL / PSIKOLOJIK
    DUYGUSAL_SORUN_YOK("Sorun yok"),
    DUYGUSAL_SEDASYON("Sedasyon"),
    DUYGUSAL_ENDISELI("Endişeli"),
    DUYGUSAL_AJITE("Ajite"),
    DUYGUSAL_DIGER("Diğer", true),

    // 4.1.17 KARDIOVASKULER
    KARDIOVASKULER_SORUN_YOK("Sorun yok"),
    KARDIOVASKULER_SIYANOZ("Siyanoz"),
    KARDIOVASKULER_HIPERTANSIYON("Hipertansiyon"),
    KARDIOVASKULER_GOGUS_AGRISI("Göğüs ağrısı"),
    KARDIOVASKULER_SENKOP("Senkop"),
    KARDIOVASKULER_CARPINTI("Çarpıntı"),
    KARDIOVASKULER_PRETIBIAL_ODEM("Pretibial Ödem"),
    KARDIOVASKULER_VARIS("Varis"),
    KARDIOVASKULER_DIGER("Diğer", true),

    // 4.1.18 SOLUNUM
    SOLUNUM_SORUN_YOK("Sorun yok"),
    SOLUNUM_HEMOPTIZI("Hemoptizi"),
    SOLUNUM_SIYANOZ("Siyanoz"),
    SOLUNUM_HIRLITILI("Hırıltılı"),
    SOLUNUM_WHEEZING("Wheezing"),
    SOLUNUM_BALGAM("Balgam"),
    SOLUNUM_OKSURME("Öksürme"),
    SOLUNUM_GUCLUGU("Solunum güçlüğü"),
    SOLUNUM_YARDIMCI_ARACLAR("Yardımcı solunum araçları", true),

    // 4.1.19 URINER
    URINER_SORUN_YOK("Sorun yok"),
    URINER_ANURI("Anuri"),
    URINER_INKONTINANS("İnkontinans"),
    URINER_POLIURI("Poliüri"),
    URINER_POLLAKURI("Pollaküri"),
    URINER_HEMATURI("Hematüri"),
    URINER_DIZURI("Dizüri"),
    URINER_NOKTURI("Noktüri"),
    URINER_DIGER("Diğer", true),

    // 4.1.20 GASTROINTESTINAL
    GASTROINTESTINAL_SORUN_YOK("Sorun yok"),
    GASTROINTESTINAL_BULANTI("Bulantı"),
    GASTROINTESTINAL_KUSMA("Kusma"),
    GASTROINTESTINAL_MELENA("Melena"),
    GASTROINTESTINAL_AGIZ_KOKUSU("Ağız kokusu"),
    GASTROINTESTINAL_HEMATEMEZ("Hematemez"),
    GASTROINTESTINAL_HEMOROID("Hemoroid"),
    GASTROINTESTINAL_ISHAL("İshal"),
    GASTROINTESTINAL_KONSTIPASYON("Konstipasyon"),
    GASTROINTESTINAL_DIGER("Diğer", true),

    // 4.1.21 BESLENME
    BESLENME_KENDISI("Kendisi besleniyor"),
    BESLENME_YARDIMA_GEREKSINIMI("Yardıma gereksinimi var"),
    BESLENME_YEMEK_SECIYOR("Yemek seçiyor"),

    // 4.1.22 ISTAH
    ISTAH_IYI("İyi"),
    ISTAH_ORTA("Orta"),
    ISTAH_ZAYIF("Zayıf"),
    ISTAH_BULANTI_KUSMA("Bulantı/kusma"),
    ISTAH_DIGER("Diğer", true),

    // 4.1.23 BESLENME SEKLI
    BESLENME_SEKLI_ORAK("Orak"),
    BESLENME_SEKLI_IV("I.V"),
    BESLENME_SEKLI_NG("NG"),
    BESLENME_SEKLI_DIGER("Diğer", true),

    // 4.1.25 UYKU
    UYKU_DUZENLI("Düzenli"),
    UYKU_DUZENSIZ("Düzensiz"),
    UYKU_UYUMA_GUCLUGU("Uyuma güçlüğü"),
    UYKU_UYUMASI_KOLAYLASTIRAN("Uyumasını kolaylaştıran uygulamalar"),

    // 4.1.26 HAREKETLILIK
    HAREKETLILIK_SORUN_YOK("Sorun yok"),
    HAREKETLILIK_IMMOBIL("İmmobil"),
    HAREKETLILIK_KISITLAMA("Kısıtlama altında"),
    HAREKETLILIK_DENGESIZLIK("Dengesizlik kuvvetsizlik"),
    HAREKETLILIK_PARALIZI("Paralizi"),
    HAREKETLILIK_FELC("Felç"),
    HAREKETLILIK_BASTON("Baston"),
    HAREKETLILIK_YURUTEC("Yürüteç"),
    HAREKETLILIK_PROTEZ("Protez"),
    HAREKETLILIK_DEGNEK("Değnek");

    private final String label;
    private final boolean aciklamaGerekli;

    EnumPkRevirAnemnezTanim(String label) {
        this(label, false);
    }

    EnumPkRevirAnemnezTanim(String label, boolean aciklamaGerekli) {
        this.label = label;
        this.aciklamaGerekli = aciklamaGerekli;
    }

    public boolean isAciklamaGerekli() {
        return aciklamaGerekli;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}