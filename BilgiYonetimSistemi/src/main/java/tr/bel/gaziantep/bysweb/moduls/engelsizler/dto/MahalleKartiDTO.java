package tr.bel.gaziantep.bysweb.moduls.engelsizler.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.17.0
 * @since 27.07.2026 13:22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MahalleKartiDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4375568341534570557L;

    private int toplamEngelli;
    private int aktifEngelli;
    private int olenEngelli;
    private int erkekSayisi;
    private int kadinSayisi;
    private int agirOzurluSayisi;
    private int toplamTamir;
    private int kursiyerSayisi;
    private int bekleyenTalep;
    private int tamamlananTalep;
    private int tamamlanmayanTalep;
    private int cozulenTalep;
    private int iptalEdilenTalep;

    private Map<String, Integer> cinsiyetGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> yasDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> engelOraniGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> engelGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> cihazTeslimGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> egitimGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> medeniGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> tamirGrubuDagilimi = new LinkedHashMap<>();
    private Map<String, Integer> talepGrubuDagilimi = new LinkedHashMap<>();
}
