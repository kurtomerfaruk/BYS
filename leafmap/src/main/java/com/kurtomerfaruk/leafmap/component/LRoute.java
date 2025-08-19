package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 15:04
 */
@Getter
@Setter
public class LRoute extends UIComponentBase {

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }
}
