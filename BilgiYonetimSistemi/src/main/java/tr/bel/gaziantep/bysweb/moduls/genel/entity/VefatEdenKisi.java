package tr.bel.gaziantep.bysweb.moduls.genel.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 09:53
 */
@Data
@AllArgsConstructor
public class VefatEdenKisi implements Serializable {
    @Serial
    private static final long serialVersionUID = 7001954464881789710L;

    private String tcKimlikNo;
    private LocalDate olumTarihi;
}
