package tr.bel.gaziantep.bysweb.webservice.kps.model.parameters;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.08.2025 16:45
 */
@Getter
@Setter
public class KodParameters  implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 7004360368503320653L;

    private List<KodParameter> Liste;

}
