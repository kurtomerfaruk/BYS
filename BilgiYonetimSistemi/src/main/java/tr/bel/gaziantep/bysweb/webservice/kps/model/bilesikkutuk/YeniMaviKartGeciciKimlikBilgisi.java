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
 * @since 19.06.2025 16:21
 */
@Getter
@Setter
public class YeniMaviKartGeciciKimlikBilgisi  implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -8462921746261039569L;

    @JsonProperty("Ad")
    private Object ad;
    @JsonProperty("AnneAd")
    private Object anneAd;
    @JsonProperty("BabaAd")
    private Object babaAd;
    @JsonProperty("BelgeNo")
    private Object belgeNo;
    @JsonProperty("Cinsiyet")
    private Object cinsiyet;
    @JsonProperty("DogumTarih")
    private KpsTarih dogumTarih;
    @JsonProperty("DuzenlenmeTarih")
    private Object duzenlenmeTarih;
    @JsonProperty("DuzenleyenIlce")
    private Object duzenleyenIlce;
    @JsonProperty("Fotograf")
    private Object fotograf;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KimlikNo")
    private long kimlikNo;
    @JsonProperty("SonGecerlilikTarih")
    private Object sonGecerlilikTarih;
    @JsonProperty("Soyad")
    private Object soyad;
    @JsonProperty("Uyruk")
    private Object uyruk;
}
