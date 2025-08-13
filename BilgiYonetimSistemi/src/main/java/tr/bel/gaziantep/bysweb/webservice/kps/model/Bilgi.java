package tr.bel.gaziantep.bysweb.webservice.kps.model;

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
public class Bilgi implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 3889976429429974571L;

    @JsonProperty("Aciklama")
    private String aciklama;
    @JsonProperty("Kod")
    private int kod;
}
