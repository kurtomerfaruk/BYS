package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class AdresSorguSonucu implements java.io.Serializable{


    @Serial
    private static final long serialVersionUID = 8585128309639750899L;
    @JsonProperty("DigerAdresBilgileri")
    private List<DigerAdresBilgileri> digerAdresBilgileri;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KimlikNo")
    private long kimlikNo;
    @JsonProperty("YerlesimYeriAdresi")
    private YerlesimYeriAdresi yerlesimYeriAdresi;
}

