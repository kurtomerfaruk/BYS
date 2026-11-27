package tr.bel.gaziantep.bysweb.moduls.ortezprotez.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 25.11.2026 11:11
 */
@Getter
@AllArgsConstructor
public class OrtTalepAsama implements Serializable {

    @Serial
    private static final long serialVersionUID = -2341319118526545031L;
    /**
     * Aşama adı (ör. Ön Muayene).
     */
    private final String ad;
    /**
     * CSS durumu: done / current / pending / rejected.
     */
    private final String cssClass;
    /**
     * Aşama altında gösterilecek tarih veya kısa not.
     */
    private final String detay;
    /**
     * Sıra numarası.
     */
    private final int sira;
}