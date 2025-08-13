package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Hata;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:08
 */
@Getter
@Setter
public class BilesikKutukSonucModel {

    @JsonProperty("Sonuc")
    private BilesikKutukSonuc sonuc;
    @JsonProperty("Hata")
    private Hata hata;
}
