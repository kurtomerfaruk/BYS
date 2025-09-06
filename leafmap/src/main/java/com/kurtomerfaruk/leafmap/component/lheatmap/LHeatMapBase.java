package com.kurtomerfaruk.leafmap.component.lheatmap;

import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.model.map.LatLng;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:50
 */
public abstract class LHeatMapBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "com.kurtomerfaruk.leafmap.component";

    public static final String DEFAULT_RENDERER = "com.kurtomerfaruk.leafmap.component.LHeatMapRenderer";

    protected enum PropertyKeys {

        points
    }

    public LHeatMapBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List<LatLng> getPoints(){
        return (List<LatLng>) getStateHelper().eval(PropertyKeys.points);
    }
    public void setPoints(final List<LatLng> points){
        getStateHelper().put(PropertyKeys.points,points);
    }
}
