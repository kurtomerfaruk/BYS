package tr.bel.gaziantep.bysweb.moduls.tabanlik.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Getter
@AllArgsConstructor
public class TabTalepAsama implements Serializable {

    @Serial
    private static final long serialVersionUID = -3181049382736450198L;

    private final String ad;
    /**
     * done / current / pending / rejected
     */
    private final String cssClass;
    private final String detay;
    private final int sira;
}
