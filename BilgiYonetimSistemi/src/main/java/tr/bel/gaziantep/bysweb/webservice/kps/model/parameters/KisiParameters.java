package tr.bel.gaziantep.bysweb.webservice.kps.model.parameters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:40
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KisiParameters implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -5185382638531835383L;

    private List<KisiParameter> kisiler;
}
