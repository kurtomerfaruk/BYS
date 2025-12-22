package tr.bel.gaziantep.bysweb.moduls.genel.dtos;

import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRapor;

import java.io.Serial;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.12.2025 09:19
 */
@Getter
@Setter
public class GnlRaporDto implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -18570466310442357L;
    private GnlRapor modul;
    private String modulAdi;
    private List<GnlRaporKolonDto> kolonlar;
    private List<GnlRaporParametreDegeriDto> parametreler;
    private EnumRaporTuru raporTuru;
    private Integer sablonId;
    private Integer kullaniciId;
    private LocalDate raporTarihi;
    private Map<String, Object> ekParametreler;

    public GnlRaporDto() {
        this.raporTarihi = LocalDate.now();
        this.ekParametreler = new HashMap<>();
    }

    public Map<String, Object> getParametreMap() {
        Map<String, Object> map = new HashMap<>();

        if (parametreler != null) {
            for (GnlRaporParametreDegeriDto param : parametreler) {
                map.put(param.getParametreAdi(), param.getDeger());
            }
        }

        map.putAll(ekParametreler);

        map.put("RAPOR_TARIHI", raporTarihi);
        map.put("KULLANICI_ID", kullaniciId);
        map.put("MODUL_ADI", modulAdi);

        return map;
    }
}
