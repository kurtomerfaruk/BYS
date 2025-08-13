package tr.bel.gaziantep.bysweb.webservice.kps.model.mahalle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 08:43
 */
@Getter
@Setter
public class MahalleSorguSonucu implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -1757842030757665595L;

    @JsonProperty("Aciklama")
    private String aciklama;
    @JsonProperty("Kod")
    private int kod;
}
