package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.Bilgi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class IlIlceMerkezAdresi implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -2316002993250164288L;
    @JsonProperty("BagimsizBolumDurum")
    private Bilgi bagimsizBolumDurum;
    @JsonProperty("BagimsizBolumTipi")
    private Bilgi bagimsizBolumTipi;
    @JsonProperty("BinaAda")
    private Object binaAda;
    @JsonProperty("BinaBlokAdi")
    private Object binaBlokAdi;
    @JsonProperty("BinaDurum")
    private Bilgi binaDurum;
    @JsonProperty("BinaKodu")
    private Long binaKodu;
    @JsonProperty("BinaNo")
    private int binaNo;
    @JsonProperty("BinaNumaratajTipi")
    private Bilgi binaNumaratajTipi;
    @JsonProperty("BinaPafta")
    private Object binaPafta;
    @JsonProperty("BinaParsel")
    private Object binaParsel;
    @JsonProperty("BinaSiteAdi")
    private Object binaSiteAdi;
    @JsonProperty("BinaYapiTipi")
    private Bilgi binaYapiTipi;
    @JsonProperty("Csbm")
    private String csbm;
    @JsonProperty("CsbmKodu")
    private int csbmKodu;
    @JsonProperty("DisKapiNo")
    private String disKapiNo;
    @JsonProperty("HataBilgisi")
    private Object hataBilgisi;
    @JsonProperty("IcKapiNo")
    private String icKapiNo;
    @JsonProperty("Il")
    private String il;
    @JsonProperty("IlKodu")
    private int ilKodu;
    @JsonProperty("Ilce")
    private String ilce;
    @JsonProperty("IlceKodu")
    private int ilceKodu;
    @JsonProperty("KatNo")
    private String katNo;
    @JsonProperty("Mahalle")
    private String mahalle;
    @JsonProperty("MahalleKodu")
    private int mahalleKodu;
    @JsonProperty("TapuBagimsizBolumNo")
    private Object tapuBagimsizBolumNo;
    @JsonProperty("YapiKullanimAmac")
    private Bilgi yapiKullanimAmac;
    @JsonProperty("YolAltiKatSayisi")
    private Object yolAltiKatSayisi;
    @JsonProperty("YolUstuKatSayisi")
    private Object yolUstuKatSayisi;
}