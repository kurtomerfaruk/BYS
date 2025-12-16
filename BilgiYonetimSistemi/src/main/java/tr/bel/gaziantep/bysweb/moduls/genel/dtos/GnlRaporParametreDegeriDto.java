package tr.bel.gaziantep.bysweb.moduls.genel.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.12.2025 09:17
 */
@Setter
@Getter
public class GnlRaporParametreDegeriDto {
    
    private Integer parametreId;
    private String parametreAdi;
    private String gorunurAdi;
    private String deger;
    private String ikinciDeger;
    private String operator;
    private String veriTipi;
    private String lookupEnumClass;
    private String sqlKosul;

}
