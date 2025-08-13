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
 * @since 19.06.2025 16:24
 */
@Getter
@Setter
public class NufusCuzdaniBilgisi implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 3656891595916916795L;
    @JsonProperty("Ad")
    public Object ad;
    @JsonProperty("AnneAd")
    public Object anneAd;
    @JsonProperty("BabaAd")
    public Object babaAd;
    @JsonProperty("CuzdanKayitNo")
    public Object cuzdanKayitNo;
    @JsonProperty("CuzdanVerilmeNeden")
    public Object cuzdanVerilmeNeden;
    @JsonProperty("DogumTarih")
    public KpsTarih dogumTarih;
    @JsonProperty("DogumYer")
    public Object dogumYer;
    @JsonProperty("HataBilgisi")
    public Bilgi hataBilgisi;
    @JsonProperty("No")
    public Object no;
    @JsonProperty("Seri")
    public Object seri;
    @JsonProperty("Soyad")
    public Object soyad;
    @JsonProperty("TCKimlikNo")
    public long tcKimlikNo;
    @JsonProperty("VerildigiIlce")
    public Object verildigiIlce;
    @JsonProperty("VerilmeTarih")
    public KpsTarih verilmeTarih;
}
