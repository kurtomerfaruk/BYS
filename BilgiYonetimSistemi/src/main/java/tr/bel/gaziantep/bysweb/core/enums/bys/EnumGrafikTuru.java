package tr.bel.gaziantep.bysweb.core.enums.bys;

import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.04.2026 10:55
 */
@Getter
public enum EnumGrafikTuru implements BaseEnum {

    COLUMN3D("Column 3d"),
    COLUMN_AXIS_BREAK("Column Axis Break"),
    CYLINDER3D("Cylinder 3d"),
    HORIZONTAL_BAR("Horizontal Bar"),
    LINE_CHART("Line Chart"),
    LIST("Liste"),
    PARETO_DIAGRAM("Pareto Diagram"),
    PIE("Pie"),
    PIE_OF_PIE("Pie Of Pie"),
    RADAR_AXIS_BREAK("Radar Axis Break"),
    SEMI_CIRCLE_DONUT("Semi Circle Donut"),
    VERTICAL_BAR("Vertical Bar");


    private final String label;

    EnumGrafikTuru(String label) {
        this.label = label;
    }

    @Override
    public String getDisplayValue() {
        return label;
    }
}
