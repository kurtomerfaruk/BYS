package tr.bel.gaziantep.bysweb.core.enums.ortezprotez;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 10:53
 */
@Getter
public enum EnumOrtBasvuruHareketDurumu implements BaseEnum {
    BEKLEMEDE("Beklemede"),
    OLUMLU("Olumlu"),
    OLCU_ICIN_RANDEVU_VERILDI("Ölçü için randevu verildi"),
    OLCU_ALINDI("Ölçü alındı"),
    OLCU_SONRASI_RANDEVU_VERILDI("Ölçü sonrası randevu verildi"),
    ODEME_ALINDI("Ödeme Alındı"),
    SUT_KODU_VERILDI("SUT kodu verildi"),
    SILIKON_TESLIM_EDILDI("Silikon Teslim Edildi"),
    MALZEME_TALEP_EDILDI("Malzeme Talep Edildi"),
    MALZEME_TALEBI_IPTAL_EDILDI("Malzeme Talebi İptal Edildi"),
    MALZEME_TALEBI_REDDEDILDI("Malzeme Talebi Reddedildi"),
    MALZEME_TALEBI_ONAYLANDI("Malzeme Talebi Onaylandı"),
    TEKNIKERE_TESLIM_EDILDI("Teknikere teslim edildi"),
    RANDEVU_OLUSTURULDU("Randevu oluşturuldu"),
    FIZIK_TEDAVI_PLANI_OLUSTURULDU("Fizik Tedavi Planı oluşturuldu."),
    URUNLER_TESLIM_EDILDI("Ürünler Teslim Edildi");

    private final String label;

    EnumOrtBasvuruHareketDurumu(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }

}