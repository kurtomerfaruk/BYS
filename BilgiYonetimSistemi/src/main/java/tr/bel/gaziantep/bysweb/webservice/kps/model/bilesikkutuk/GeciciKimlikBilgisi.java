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
 * @since 19.06.2025 16:23
 */
@Getter
@Setter
public class GeciciKimlikBilgisi implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -7863816429872315856L;

    @JsonProperty("Ad")
    private Object ad;
    @JsonProperty("AnneAd")
    private Object anneAd;
    @JsonProperty("BabaAd")
    private Object babaAd;
    @JsonProperty("BelgeNo")
    private Object belgeNo;
    @JsonProperty("Cinsiyet")
    private Bilgi cinsiyet;
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
    @JsonProperty("KanGrubu")
    private Object kanGrubu;
    @JsonProperty("OncekiSoyad")
    private Object oncekiSoyad;
    @JsonProperty("SonGecerlilikTarih")
    private Object sonGecerlilikTarih;
    @JsonProperty("Soyad")
    private Object soyad;
    @JsonProperty("TCKimlikNo")
    private long tCKimlikNo;
}
