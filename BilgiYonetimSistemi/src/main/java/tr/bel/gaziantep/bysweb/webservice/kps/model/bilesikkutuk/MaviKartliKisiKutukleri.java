package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Hata;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:13
 */
@Getter
@Setter
public class MaviKartliKisiKutukleri  implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -5432895143081786116L;

    @JsonProperty("HataBilgisi")
    private Hata hataBilgisi;
    @JsonProperty("KisiBilgisi")
    private KisiBilgisi kisiBilgisi;
    @JsonProperty("MaviKartBilgisi")
    private MaviKartBilgisi maviKartBilgisi;
    @JsonProperty("YeniMaviKartBilgisi")
    private YeniMaviKartBilgisi yeniMaviKartBilgisi;
    @JsonProperty("YeniMaviKartFotoBilgisi")
    private YeniMaviKartFotoBilgisi yeniMaviKartFotoBilgisi;
    @JsonProperty("YeniMaviKartGeciciKimlikBilgisi")
    private YeniMaviKartGeciciKimlikBilgisi yeniMaviKartGeciciKimlikBilgisi;
}
