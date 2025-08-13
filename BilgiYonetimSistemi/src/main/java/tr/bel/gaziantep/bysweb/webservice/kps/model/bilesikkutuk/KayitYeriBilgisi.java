package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:17
 */
@Getter
@Setter
public class KayitYeriBilgisi  implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -5980625548070746209L;

    @JsonProperty("AileSiraNo")
    private int aileSiraNo;
    @JsonProperty("BireySiraNo")
    private int bireySiraNo;
    @JsonProperty("Cilt")
    private Bilgi cilt;
    @JsonProperty("Il")
    private Bilgi il;
    @JsonProperty("Ilce")
    private Bilgi ilce;
}
