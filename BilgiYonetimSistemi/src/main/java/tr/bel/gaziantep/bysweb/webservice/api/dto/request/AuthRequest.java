package tr.bel.gaziantep.bysweb.webservice.api.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:06
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    private String appKey;
    private String appSecret;
}
