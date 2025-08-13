package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:11
 */
@Getter
@Setter
public class BilesikKutukSorguSonucu implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4131496475319953214L;

    @JsonProperty("DoluBilesenler")
    private List<Integer> doluBilesenler;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KimlikNo")
    private Long kimlikNo;
    @JsonProperty("MaviKartliKisiKutukleri")
    private MaviKartliKisiKutukleri maviKartliKisiKutukleri;
    @JsonProperty("TCVatandasiKisiKutukleri")
    private TCVatandasiKisiKutukleri tcVatandasiKisiKutukleri;
    @JsonProperty("YabanciKisiKutukleri")
    private YabanciKisiKutukleri yabanciKisiKutukleri;
}
