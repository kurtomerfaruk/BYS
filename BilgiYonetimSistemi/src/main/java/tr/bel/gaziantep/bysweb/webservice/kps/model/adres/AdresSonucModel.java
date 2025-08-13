package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;


/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class AdresSonucModel implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -7416873044569313375L;
    @JsonProperty("Sonuc")
    private AdresSonuc sonuc;
    @JsonProperty("Hata")
    private Object hata;
}

