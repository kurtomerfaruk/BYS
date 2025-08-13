package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Hata;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:10
 */
@Getter
@Setter
public class BilesikKutukSonuc implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 5405781441413416294L;

    @JsonProperty("SorguSonucu")
    private List<BilesikKutukSorguSonucu> sorguSonucu;
    @JsonProperty("HataBilgisi")
    private Hata hataBilgisi;
}
