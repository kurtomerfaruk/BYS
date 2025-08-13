package tr.bel.gaziantep.bysweb.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumOperator;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 08:49
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class DynamicReportModel implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1274374340815993117L;

    private String columnKey;
    private String columnValue;
    private EnumOperator operator;
    private String value;
    private String valueTo;
}
