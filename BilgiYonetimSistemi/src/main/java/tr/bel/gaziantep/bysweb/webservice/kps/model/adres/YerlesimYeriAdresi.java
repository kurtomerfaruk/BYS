package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsTarih;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class YerlesimYeriAdresi implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = -7909414549522369953L;

    @JsonProperty("AcikAdres")
    private String acikAdres;
    @JsonProperty("AdresNo")
    private Long adresNo;
    @JsonProperty("AdresTip")
    private Bilgi adresTip;
    @JsonProperty("BeldeAdresi")
    private Object beldeAdresi;
    @JsonProperty("BeyanTarihi")
    private KpsTarih beyanTarihi;
    @JsonProperty("BeyandaBulunanKimlikNo")
    private Object beyandaBulunanKimlikNo;
    @JsonProperty("HataBilgisi")
    private Object hataBilgisi;
    @JsonProperty("IlIlceMerkezAdresi")
    private IlIlceMerkezAdresi ilIlceMerkezAdresi;
    @JsonProperty("KoyAdresi")
    private Object koyAdresi;
    @JsonProperty("TasinmaNeden")
    private Object tasinmaNeden;
    @JsonProperty("TasinmaNedenAciklama")
    private Object tasinmaNedenAciklama;
    @JsonProperty("TasinmaTarihi")
    private KpsTarih tasinmaTarihi;
    @JsonProperty("TescilTarihi")
    private KpsTarih tescilTarihi;
    @JsonProperty("YurtDisiAdresi")
    private Object yurtDisiAdresi;
}