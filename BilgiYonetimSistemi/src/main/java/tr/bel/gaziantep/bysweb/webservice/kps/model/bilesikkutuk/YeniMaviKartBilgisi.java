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
 * @since 19.06.2025 16:19
 */
@Getter
@Setter
public class YeniMaviKartBilgisi implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = -4072457021540921142L;

    @JsonProperty("Ad")
    private String ad;
    @JsonProperty("AnneAd")
    private String anneAd;
    @JsonProperty("BabaAd")
    private String babaAd;
    @JsonProperty("Cinsiyet")
    private Bilgi cinsiyet;
    @JsonProperty("DogumTarih")
    private KpsTarih dogumTarih;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KimlikNo")
    private long kimlikNo;
    @JsonProperty("SeriNo")
    private String seriNo;
    @JsonProperty("SonGecerlilikTarih")
    private KpsTarih sonGecerlilikTarih;
    @JsonProperty("Soyad")
    private String soyad;
    @JsonProperty("Uyruk")
    private Bilgi uyruk;
    @JsonProperty("VerenMakam")
    private String verenMakam;
}
