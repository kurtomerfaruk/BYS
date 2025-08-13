package tr.bel.gaziantep.bysweb.webservice.kps.model.mahalle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 08:42
 */
@Getter
@Setter
public class MahalleSonucModel implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -6068332821203796327L;

    @JsonProperty("Sonuc")
    private List<MahalleSorguSonucu> sonuc;
    @JsonProperty("Hata")
    private Object hata;
}
