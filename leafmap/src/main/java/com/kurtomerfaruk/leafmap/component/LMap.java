package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 11:55
 */
@Getter
@Setter
public class LMap extends UIComponentBase {

    private String center;
    private Integer zoom;
    private String width;
    private String height;
    private String style;
    private String styleClass;
    private String widgetVar;

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

}