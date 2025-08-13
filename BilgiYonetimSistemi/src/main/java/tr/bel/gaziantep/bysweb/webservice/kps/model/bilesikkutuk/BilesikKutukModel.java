package tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:10
 */
@Getter
@Setter
public class BilesikKutukModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 7428274603029667590L;
    private Long tcKimlikNo;
    private String kimlikSeriNo;
    private LocalDate sonGecerlilikTarihi;
    private String uyruk;
    private String ad;
    private String soyad;
    private String anneAd;
    private String babaAd;
    private Integer cinsiyet;
    private String cinsiyetAciklama;
    private String dogumYeri;
    private LocalDate dogumTarihi;
    private Integer durum;
    private String durumAciklama;
    private Integer medeniHal;
    private String medeniHalAciklama;
    private LocalDate olumTarihi;
    private Integer nufusIlceKod;
    private String nufusIlceAciklama;
    private Integer ciltKod;
    private String cilt;
    private Integer aileSiraNo;
    private Integer bireyNo;
    private String hataBilgisi;

}
