package tr.bel.gaziantep.bysweb.webservice.kps.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KisiParameter implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = -6055512875537394133L;
    private long tcKimlikNo;
    private int dogumYil;
    private int dogumAy;
    private int dogumGun;
}
