package tr.bel.gaziantep.bysweb.webservice.api.dto.evimdunyalarabedel;

import lombok.*;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbHizmetTuru;

import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 12.03.2026 23:19
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EdbTalepDTO {
    private LocalDateTime tarih;
    private String talepTuru;
    private String talepTipi;
    private String durum;
    private String durumAciklama;
    private String aciklama;
    private EnumEdbHizmetTuru talepEdilenHizmetler;

}
