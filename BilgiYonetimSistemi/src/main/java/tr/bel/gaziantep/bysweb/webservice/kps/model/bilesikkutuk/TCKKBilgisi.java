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
 * @since 19.06.2025 16:22
 */
@Getter
@Setter
public class TCKKBilgisi implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 6838654941460481290L;
    @JsonProperty("Ad")
    private String ad;
    @JsonProperty("AnneAd")
    private String anneAd;
    @JsonProperty("BabaAd")
    private String babaAd;
    @JsonProperty("Cinsiyet")
    private Bilgi cinsiyet;
    @JsonProperty("DogumTarih")
    private KpsTarih dogumTarih;
    @JsonProperty("HataBilgisi")
    private Bilgi hataBilgisi;
    @JsonProperty("KanGrubu")
    private Object kanGrubu;
    @JsonProperty("SeriNo")
    private String seriNo;
    @JsonProperty("SonGecerlilikTarih")
    private KpsTarih sonGecerlilikTarih;
    @JsonProperty("Soyad")
    private String soyad;
    @JsonProperty("SurucuBelgeYuklendiMi")
    private boolean surucuBelgeYuklendiMi;
    @JsonProperty("SurucuBelgeYuklenmeTarih")
    private Object surucuBelgeYuklenmeTarih;
    @JsonProperty("TCKimlikNo")
    private long tCKimlikNo;
    @JsonProperty("VerenMakam")
    private String verenMakam;

}
