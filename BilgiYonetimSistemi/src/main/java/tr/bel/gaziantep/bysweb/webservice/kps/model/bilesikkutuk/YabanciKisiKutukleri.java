package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:11
 */
@Getter
@Setter
public class YabanciKisiKutukleri implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 3219602548106299492L;
    @JsonProperty("HataBilgisi")
    private Object hataBilgisi;
    @JsonProperty("KisiBilgisi")
    private KisiBilgisi kisiBilgisi;

}
