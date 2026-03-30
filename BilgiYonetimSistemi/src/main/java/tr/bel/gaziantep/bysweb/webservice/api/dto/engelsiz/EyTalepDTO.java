package tr.bel.gaziantep.bysweb.webservice.api.dto.engelsiz;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 19.02.2026 22:39
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EyTalepDTO {
    private String konu;
    private LocalDateTime tarih;
    private String talepTuru;
    private String talepTipi;
    private String durum;
    private String durumAciklama;
    private String aciklama;


}
