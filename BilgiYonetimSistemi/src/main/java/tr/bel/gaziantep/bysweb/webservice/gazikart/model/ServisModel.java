package tr.bel.gaziantep.bysweb.webservice.gazikart.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:50
 */
@Getter
@Setter
public class ServisModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 3826554024679815289L;
    private String IDENTITY_NO;
    private String TEL_MOBILE;
    private String DESCRIPTION;
    private String DISABLED_DEGREE;
    private String DISABLED_TYPE;
    private String DISABLED_TYPE_ID;
    private String RELATIONSHIP;
    private String RAPOR_ILK_TARIH;
    private String RAPOR_SON_TARIH;
    private String RAPOR_NO;
    private String BIRTH_DATE;
}
