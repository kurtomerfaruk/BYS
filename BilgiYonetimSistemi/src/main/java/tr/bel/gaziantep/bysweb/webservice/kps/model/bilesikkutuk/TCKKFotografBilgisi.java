package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:27
 */
@Getter
@Setter
public class TCKKFotografBilgisi implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 3743847957181430826L;
    @JsonProperty("Fotograf")
    private Object fotograf;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("TCKimlikNo")
    private Long tCKimlikNo;
}
