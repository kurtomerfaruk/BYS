package tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 08:39
 */
@Getter
public enum EnumSyFiltreAnahtari implements BaseEnum {
    EVET_HAYIR("Evet/Hayır"),
    MENU_TUR("Menü Tür"),
    FILTRE_TURU("Filtre Türü"),
    FILTRE_ANAHTARI("Filtre Anahtarı"),
    VERI_TIPI("Veri Tipi"),
    FILTRE_ESLESTIRME_MODU("Filtre Eşleştirme Modu"),
    KULLANICI_TURU("Kullanıcı Türü"),
    UYRUK("Uyruk"),
    ILCE("İlçe"),
    IL("İl"),
    CINSIYET("Cinsiyet"),
    MEDENI_DURUM("Medeni Durum"),
    KISI_DURUM("Kişi Durum"),
    EKLEME_YERI("Ekleme Yeri"),
    GNLANKET_TURU("Genel Anket Türü"),
    ENGELSIZ_ANKET_DURUMU("Engelsiz Anket Durumu"),
    TALEP_TURU("Talep Türü"),
    TALEP_TIPI("Talep Tipi"),
    TALEP_DURUMU("Talep Durumu"),
    EYTALEP_KONU("Engelsiz Talep Konu"),
    EYARAC_BILGISI("Engelsiz Araç Bilgisi"),
    EYARAC_ARIZA_TURU("Engelsiz Arıza Türü"),
    EYARAC_CIHAZ_DURUMU("Engelsiz Cihaz Durumu"),
    EYDEGERLENDIRME_DURUMU("Engelsiz Değerlendirme Durumu"),
    GNLDEVAM_DURUMU("Devam Durumu"),
    AYBIRIM("Aktif Yaşam Birim"),
    EDBHIZMET_TURU("Evde Yaşama Destek Hizmet Türü"),
    EDBBASVURU_DURUMU("Evde Yaşama Destek Başvuru Durumu"),
    SHDANISMANLIK_HIZMETI("Sağlık Hizmetleri Danışmanlık Hizmeti"),
    SHOBEZITE_HIZMETI("Sağlık Hizmetleri Obezite Hizmet"),
    SORU_TURU("Soru Türü"),
    HFFIRMA_TURU("Hafriyat Firma Türü"),
    HFMAL_CINSI("Hafriyat Mal Cinsi"),
    BELEDIYE("Belediye"),
    HFTAHSILAT_TURU("Hafriyat Tahsilat Türü"),
    DUYURU_TUR("Duyuru Tür"),
    ORTENGEL_OLUSUM("Ortez/Protez Engel Oluşum"),
    ORTOLCU_SABLON("Ölçü Şablonları"),
    ORTOLCU_SABLON_ALAN_TURU("Ortez/Protez Alan Türü"),
    ORTBASVURU_DURUMU("Ortez/Protez Başvuru Durumu"),
    ORTBASVURU_HAREKET_DURUMU("Ortez/Protez Başvuru Hareket Durumu"),
    ORTOLCU_DURUM("Ortez/Protez Ölçü Durum"),
    ORTSTOK_BIRIM("Ortez/Protez Stok Birim"),
    ORTMALZEME_TALEP_ONAY_DURUMU("Ortez/Protez Malzeme Talep Onay Durumu"),
    ORTFIZIK_TEDAVI_DURUM("Ortez/Protez Fizik Tedavi Durumu"),
    METALEP_DURUMU("Moral Evi Talep Durumu"),
    GNLGUN("Gün");

    private final String label;

    EnumSyFiltreAnahtari(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
