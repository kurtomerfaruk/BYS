package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsTarih;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:14
 */
@Getter
@Setter
public class KisiBilgisi implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -6175148070254059465L;

    @JsonProperty("AnneTCKimlikNo")
    public Object anneTCKimlikNo;
    @JsonProperty("BabaTCKimlikNo")
    public Object babaTCKimlikNo;
    @JsonProperty("DogumYerKod")
    public Object dogumYerKod;
    @JsonProperty("DurumBilgisi")
    public DurumBilgisi durumBilgisi;
    @JsonProperty("EsTCKimlikNo")
    public Object esTCKimlikNo;
    @JsonProperty("GercekKisiKimlikNo")
    public Object gercekKisiKimlikNo;
    @JsonProperty("HataBilgisi")
    public Bilgi hataBilgisi;
    @JsonProperty("KazanilanTCKimlikNo")
    public Object kazanilanTCKimlikNo;
    @JsonProperty("KimlikNo")
    public long kimlikNo;
    @JsonProperty("TemelBilgisi")
    public TemelBilgisi temelBilgisi;
    @JsonProperty("Ulke")
    public Object ulke;
    @JsonProperty("AnneUstSoyBulunamadi")
    public Object anneUstSoyBulunamadi;
    @JsonProperty("BabaUstSoyBulunamadi")
    public Object babaUstSoyBulunamadi;
    @JsonProperty("KayitYeriBilgisi")
    public KayitYeriBilgisi kayitYeriBilgisi;
    @JsonProperty("OncekiYabanciKimlikNo")
    public Object oncekiYabanciKimlikNo;
    @JsonProperty("TCKimlikNo")
    public Long tcKimlikNo;
    @JsonProperty("AnneKimlikNo")
    public Object anneKimlikNo;
    @JsonProperty("BabaKimlikNo")
    public Object babaKimlikNo;
    @JsonProperty("BitisTarihiBelirsizOlmaNeden")
    public Object bitisTarihiBelirsizOlmaNeden;
    @JsonProperty("DogumTarih")
    public KpsTarih dogumTarih;
    @JsonProperty("EsKimlikNo")
    public Object esKimlikNo;
    @JsonProperty("IzinBaslangicTarih")
    public String izinBaslangicTarih;
    @JsonProperty("IzinBitisTarih")
    public String izinBitisTarih;
    @JsonProperty("IzinDuzenlenenIl")
    public Object izinDuzenlenenIl;
    @JsonProperty("IzinNo")
    public Object izinNo;
    @JsonProperty("KaynakBirim")
    public Object kaynakBirim;
    @JsonProperty("OlumTarih")
    public KpsTarih olumTarih;
    @JsonProperty("Statu")
    public Object statu;
    @JsonProperty("Uyruk")
    public Object uyruk;
    @JsonProperty("VeliVasiTur")
    public Object veliVasiTur;
    @JsonProperty("VeliVasiVar")
    public Object veliVasiVar;
}
