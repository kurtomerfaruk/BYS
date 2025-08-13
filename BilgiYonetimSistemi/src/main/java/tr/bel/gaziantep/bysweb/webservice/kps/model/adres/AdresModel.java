package tr.bel.gaziantep.bysweb.webservice.kps.model.adres;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:12
 */
@Getter
@Setter
public class AdresModel implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 5882936075053978256L;
    private Long TcKimlikNo;
    private Long binaKodu;
    private Integer binaNo;
    private String acikAdres;
    private Long adresNo;
    private Integer mahalleKodu;
    private String mahalleAciklama;
    private Integer ilceKodu;
    private String ilceAciklama;
    private String disKapiNo;
    private String icKapiNo;
    private Integer sehirKodu;
    private String sehirAciklama;
    private String hataBilgisi;
}