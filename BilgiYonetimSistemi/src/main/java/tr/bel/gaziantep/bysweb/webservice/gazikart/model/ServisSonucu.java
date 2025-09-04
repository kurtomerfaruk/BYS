package tr.bel.gaziantep.bysweb.webservice.gazikart.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:51
 */
@Getter
@Setter
public class ServisSonucu implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 4020072707888612725L;

    private String datetime;
    private int count;
    private List<ServisModel> data;
}
