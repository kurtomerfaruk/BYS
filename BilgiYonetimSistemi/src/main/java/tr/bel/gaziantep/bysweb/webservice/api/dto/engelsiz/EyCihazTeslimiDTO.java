package tr.bel.gaziantep.bysweb.webservice.api.dto.engelsiz;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 19.02.2026 23:35
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EyCihazTeslimiDTO {
    private String arac;
    private String aciklama;
    private LocalDateTime verilisTarihi;
    private String cihazDurumu;
    private String geriAlimAciklamasi;
    private LocalDateTime geriAlimTarihi;
}
