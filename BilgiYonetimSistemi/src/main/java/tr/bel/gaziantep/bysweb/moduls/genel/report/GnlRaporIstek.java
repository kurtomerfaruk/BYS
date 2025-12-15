package tr.bel.gaziantep.bysweb.moduls.genel.report;

import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporModul;

import java.io.Serial;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 13:35
 */
@Getter
@Setter
public class GnlRaporIstek implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 890689451906388228L;


    private GnlRaporModul modul;
    private String modulAdi;
    private List<RaporKolonDto> kolonlar;
    private List<ParametreDegeriDto> parametreler;
    private String ciktiTipi; // PDF, EXCEL, CSV
    private Integer sablonId; // Kaydedilmiş şablon kullanılıyorsa
    private Integer kullaniciId;
    private LocalDate raporTarihi;
    private Map<String, Object> ekParametreler;

    // Default constructor
    public GnlRaporIstek() {
        this.raporTarihi =LocalDate.now();
        this.ekParametreler = new HashMap<>();
    }

    // Parametre map'ini döndüren yardımcı metod (JasperReport için)
    public Map<String, Object> getParametreMap() {
        Map<String, Object> map = new HashMap<>();

        // Parametreleri map'e ekle
        if (parametreler != null) {
            for (ParametreDegeriDto param : parametreler) {
                map.put(param.getParametreAdi(), param.getDeger());
            }
        }

        // Ek parametreleri ekle
        map.putAll(ekParametreler);

        // Sistem parametreleri
        map.put("RAPOR_TARIHI", raporTarihi);
        map.put("KULLANICI_ID", kullaniciId);
        map.put("MODUL_ADI", modulAdi);

        return map;
    }

    // Rapor kolonu için DTO
    @Getter
    @Setter
    public static class RaporKolonDto {
        // Getter ve Setter metodları
        private Integer id;
        private String alanAdi;
        private String gorunurAdi;
        private String veriTipi;
        private Integer genislik;
        private String format;

    }

    // Parametre değeri için DTO
    @Getter
    @Setter
    public static class ParametreDegeriDto {
        // Getter ve Setter metodları
        private Integer parametreId;
        private String parametreAdi;
        private String gorunurAdi;
        private String deger;
        private String ikinciDeger; // BETWEEN operatörü için
        private String operator; // =, >, <, LIKE, BETWEEN, IN, etc.
        private String veriTipi;
        private String lookupEnumClass;
        private String sqlKosul;
    }
}
