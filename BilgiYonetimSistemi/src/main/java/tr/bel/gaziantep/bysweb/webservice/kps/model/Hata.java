package tr.bel.gaziantep.bysweb.webservice.kps.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:09
 */
@Getter
@Setter
public class Hata {

    @JsonProperty("HataKodu")
    private int hataKodu;
    @JsonProperty("HataMesaji")
    private String hataMesaji;
}
