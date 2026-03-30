package tr.bel.gaziantep.bysweb.webservice.api.dto.evimdunyalarabedel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 12.03.2026 23:39
 */
@Getter
@Setter
public class EdbBasvuruDTO {
    private String tcKimlikNo;
    private LocalDate dogumTarihi;
    private String ad;
    private String soyad;
    private String dogumYeri;
    private String ilce;
    private String mahalle;
    private String adres;
    private String cinsiyet;
    private String medeniDurum;
    private String telefon;
    private String telefon2;
    private String koordinat;
    private LocalDateTime eklemeTarihi;
    private LocalDateTime guncellemeTarihi;
    private LocalDateTime basvuruTarihi;
    private String kisiTuru;
    private String hizmetDurumu;
    private String basvuruDurumu;
    private List<EdbTalepDTO> talep;

}
