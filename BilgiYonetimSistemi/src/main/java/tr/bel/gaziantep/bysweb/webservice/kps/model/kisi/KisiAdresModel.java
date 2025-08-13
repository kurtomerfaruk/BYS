package tr.bel.gaziantep.bysweb.webservice.kps.model.kisi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 09:53
 */
@Getter
@Setter
public class KisiAdresModel implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -1243504990771245384L;
    @JsonProperty("X")
    private String x;
    @JsonProperty("Y")
    private String y;
    @JsonProperty("IlceKodu")
    private Integer ilceKodu;
    @JsonProperty("Ilce")
    private String ilce;
    @JsonProperty("MahalleKod")
    private Integer mahalleKodu;
    @JsonProperty("Mahalle")
    private String mahalle;
    @JsonProperty("BinaNo")
    private String binaNo;
    @JsonProperty("BinaKodu")
    private String binaKodu;
    @JsonProperty("IcKapiNo")
    private String icKapiNo;
    @JsonProperty("DisKapiNo")
    private String disKapiNo;
    @JsonProperty("Csbm")
    private String csbm;
    @JsonProperty("CsbmKod")
    private String csbmKodu;
    @JsonProperty("AdresNo")
    private String adresNo;
    @JsonProperty("AcikAdres")
    private String acikAdres;
    @JsonProperty("TcKimlikNo")
    private String tcKimlikNo;
    @JsonProperty("Adi")
    private String adi;
    @JsonProperty("Soyadi")
    private String soyadi;
    @JsonProperty("Cinsiyet")
    private Integer cinsiyet;
    @JsonProperty("CinsiyetAciklama")
    private String cinsiyetAciklama;
    @JsonProperty("MedeniHal")
    private Integer medeniHal;
    @JsonProperty("MedeniHalAciklama")
    private String medeniHalAciklama;
    @JsonProperty("Durum")
    private Integer durum;
    @JsonProperty("DurumAciklama")
    private String durumAciklama;
    @JsonProperty("DogumTarihi")
    private String dogumTarihi;
    @JsonProperty("BabaAdi")
    private String babaAdi;
    @JsonProperty("AnneAdi")
    private String anneAdi;
    @JsonProperty("DogumYeri")
    private String dogumYeri;
    @JsonProperty("Hata")
    private String hata;
}
