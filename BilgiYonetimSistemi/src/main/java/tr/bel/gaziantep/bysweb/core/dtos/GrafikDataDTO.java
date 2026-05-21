package tr.bel.gaziantep.bysweb.core.dtos;

import com.kurtomerfaruk.amchartfaces.model.ChartModel;
import lombok.*;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGrafik;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 27.04.2026 09:30
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrafikDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6008884671616733662L;
    private ChartModel chartModel;
    private SyGrafik syGrafik;
}
