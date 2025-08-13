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
 * @since 19.06.2025 16:16
 */
@Getter
@Setter
public class TemelBilgisi  implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -3515717653043942024L;

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
    @JsonProperty("DogumYer")
    private String dogumYer;
    @JsonProperty("Soyad")
    private String soyad;
}
