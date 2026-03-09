package tr.bel.gaziantep.bysweb.webservice.api.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 19.02.2026 21:15
 */
@Getter
@Setter
public class PageRequest {

    private int page = 0;
    private int size = 100;
}
