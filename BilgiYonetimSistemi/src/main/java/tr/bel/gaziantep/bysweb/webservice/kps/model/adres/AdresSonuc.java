package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class AdresSonuc implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1679942116785847071L;
    @JsonProperty("SorguSonucu")
    private List<AdresSorguSonucu> sorguSonucu;
    @JsonProperty("HataBilgisi")
    private Object hataBilgisi;

}
