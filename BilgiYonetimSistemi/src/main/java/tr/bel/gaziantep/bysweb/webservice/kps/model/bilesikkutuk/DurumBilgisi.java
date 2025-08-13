package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsTarih;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:14
 */
@Getter
@Setter
public class DurumBilgisi  implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 5206836175117055068L;

    @JsonProperty("Durum")
    private Bilgi durum;
    @JsonProperty("MedeniHal")
    private Bilgi medeniHal;
    @JsonProperty("OlumTarih")
    private KpsTarih olumTarih;
    @JsonProperty("Din")
    private Bilgi din;
}
