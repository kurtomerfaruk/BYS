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
 * @since 19.06.2025 16:18
 */
@Getter
@Setter
public class MaviKartBilgisi implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -8913621444126810492L;

    @JsonProperty("Ad")
    private Object ad;
    @JsonProperty("AnneAd")
    private Object anneAd;
    @JsonProperty("BabaAd")
    private Object babaAd;
    @JsonProperty("Birim")
    private Object birim;
    @JsonProperty("Cinsiyet")
    private Bilgi cinsiyet;
    @JsonProperty("DogumTarih")
    private KpsTarih dogumTarih;
    @JsonProperty("DogumYer")
    private Object dogumYer;
    @JsonProperty("DogumYerKod")
    private Object dogumYerKod;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KartKayitNo")
    private Object kartKayitNo;
    @JsonProperty("KimlikNo")
    private long kimlikNo;
    @JsonProperty("MedeniHal")
    private Object medeniHal;
    @JsonProperty("No")
    private Object no;
    @JsonProperty("OncekiSoyad")
    private Object oncekiSoyad;
    @JsonProperty("Seri")
    private Object seri;
    @JsonProperty("Soyad")
    private Object soyad;
    @JsonProperty("Uyruk")
    private Object uyruk;
    @JsonProperty("VerilisNeden")
    private Object verilisNeden;
    @JsonProperty("VerilmeTarih")
    private KpsTarih verilmeTarih;
}
