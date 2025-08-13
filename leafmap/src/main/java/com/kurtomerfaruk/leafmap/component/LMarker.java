package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 13:42
 */
@Getter
@Setter
public class LMarker extends UIComponentBase {

    private Double lat;
    private Double lng;
    private String title;
    private Boolean draggable;

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

}
