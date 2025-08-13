package tr.bel.gaziantep.bysweb.webservice.kps.model.koordinat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Hata;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.06.2025 10:42
 */
@Getter
@Setter
public class KoordinatModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 962869376167863294L;
    @JsonProperty("X")
    private String latitude;
    @JsonProperty("Y")
    private String longitude;
    @JsonProperty("IlceKodu")
    private int ilceKodu;
    @JsonProperty("Hata")
    private Hata hata;
    @JsonProperty("Message")
    private String message;
}
