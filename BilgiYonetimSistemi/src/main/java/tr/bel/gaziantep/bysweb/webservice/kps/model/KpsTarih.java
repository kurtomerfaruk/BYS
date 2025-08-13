package tr.bel.gaziantep.bysweb.webservice.kps.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:15
 */
@Getter
@Setter
public class KpsTarih  implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 3454577942138803444L;
    @JsonProperty("Ay")
    private Integer ay;
    @JsonProperty("Gun")
    private Integer gun;
    @JsonProperty("Yil")
    private Integer yil;
}
