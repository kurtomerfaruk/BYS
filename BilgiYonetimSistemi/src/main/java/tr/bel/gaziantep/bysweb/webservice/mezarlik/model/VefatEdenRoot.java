package tr.bel.gaziantep.bysweb.webservice.mezarlik.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 09:48
 */
@Data
public class VefatEdenRoot implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 5364267580066893942L;

    @JsonProperty("Liste")
    private List<VefatEden> liste;
    @JsonProperty("Hata")
    private Object hata;
}
