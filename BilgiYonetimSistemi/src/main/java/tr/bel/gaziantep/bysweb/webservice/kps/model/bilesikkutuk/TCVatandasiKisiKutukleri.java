package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:22
 */
@Getter
@Setter
public class TCVatandasiKisiKutukleri implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 7167795136449841583L;

    @JsonProperty("GeciciKimlikBilgisi")
    private GeciciKimlikBilgisi geciciKimlikBilgisi;
    @JsonProperty("HataBilgisi")
    private Object hataBilgisi;
    @JsonProperty("KisiBilgisi")
    private KisiBilgisi kisiBilgisi;
    @JsonProperty("NufusCuzdaniBilgisi")
    private NufusCuzdaniBilgisi nufusCuzdaniBilgisi;
    @JsonProperty("TCKKBilgisi")
    private TCKKBilgisi tCKKBilgisi;
    @JsonProperty("TCKKFotografBilgisi")
    private TCKKFotografBilgisi tCKKFotografBilgisi;
}
