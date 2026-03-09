package tr.bel.gaziantep.bysweb.webservice.api.dto.engelsizkariyermerkezi;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 9.03.2026 23:15
 */
@Getter
@Setter
public class EkmKursiyerDto {
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
    private String devamDurumu;
    private List<String> kurs;
    private LocalDateTime eklemeTarihi;
    private LocalDateTime guncellemeTarihi;
}
