package tr.bel.gaziantep.bysweb.moduls.genel.report;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.12.2025 15:52
 */
@Getter
@Setter
public class DynamicColumn {
    private String field;
    private String title;
    private int width;
    private Object clazz;

    public DynamicColumn(String field, String title, int width,Object clazz) {
        this.field = field;
        this.title = title;
        this.width = width;
        this.clazz = clazz;
    }
}