package com.kurtomerfaruk.leafmap.component.lheatmap;

import com.kurtomerfaruk.leafmap.utils.Constants;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.event.BehaviorEvent;

import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:49
 */
@ResourceDependency(library = Constants.LIBRARY, name = "heatmap/leaflet-heat.js")
public class LHeatMap extends LHeatMapBase{
    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return Map.of();
    }
}
