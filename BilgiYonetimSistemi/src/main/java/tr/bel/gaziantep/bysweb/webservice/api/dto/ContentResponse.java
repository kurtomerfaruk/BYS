package tr.bel.gaziantep.bysweb.webservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 9.03.2026 20:06
 */
@Getter
@Setter
@AllArgsConstructor
public class ContentResponse<T> {
    private T content;
}
