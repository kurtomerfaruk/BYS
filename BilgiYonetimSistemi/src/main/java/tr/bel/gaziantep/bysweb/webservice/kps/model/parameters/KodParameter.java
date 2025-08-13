package tr.bel.gaziantep.bysweb.webservice.kps.model.parameters;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.08.2025 16:45
 */
@Getter
@Setter
public class KodParameter implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -4854620029395732478L;

    private Long Kod;
}
