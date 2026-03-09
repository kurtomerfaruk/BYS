package tr.bel.gaziantep.bysweb.webservice.api.dto.engelsiz;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 19.02.2026 21:20
 */
@Getter
@Setter
public class EyKisiDTO {
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
    private List<EyTalepDto> talep;
    private List<String> engelGrubu;
    private Integer toplamVucutKayipOrani;
    private List<String> maddeKullanimi;
    private LocalDateTime eklemeTarihi;
    private LocalDateTime guncellemeTarihi;
    private List<EyCihazTeslimiDTO> cihazTeslimi;
}
