package tr.bel.gaziantep.bysweb.moduls.genel.dtos;

import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlRaporGrupTuru;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyVeriTipi;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.12.2025 09:19
 */
@Getter
@Setter
public class GnlRaporKolonDto {

    private Integer id;
    private String alanAdi;
    private String gorunurAdi;
    private EnumSyVeriTipi veriTipi;
    private Integer genislik;
    private String format;
    private Integer gruplamaSirasi;
    private EnumGnlRaporGrupTuru grupTuru;
}
