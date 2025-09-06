package com.kurtomerfaruk.leafmap.component.lheatmap;

import com.kurtomerfaruk.leafmap.utils.Constants;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponentBase;
import org.primefaces.model.map.LatLng;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:49
 */
@ResourceDependency(library = Constants.LIBRARY, name = "heatmap/leaflet-heat.js")
public class LHeatMap extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "com.kurtomerfaruk.leafmap.component";


    protected enum PropertyKeys {
        points, blur, radius, maxZoom, max
    }

    public LHeatMap() {
        setRendererType(null);
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

    public int getRadius() {
        return (Integer) getStateHelper().eval(PropertyKeys.radius, 20);
    }

    public void setRadius(final int radius) {
        getStateHelper().put(PropertyKeys.radius, radius);
    }

    public int getBlur() {
        return (Integer) getStateHelper().eval(PropertyKeys.blur, 15);
    }

    public void setBlur(final int blur) {
        getStateHelper().put(PropertyKeys.blur, blur);
    }

    public int getMaxZoom() {
        return (Integer) getStateHelper().eval(PropertyKeys.maxZoom, 10);
    }

    public void setMaxZoom(final int maxZoom) {
        getStateHelper().put(PropertyKeys.maxZoom, maxZoom);
    }

    public double getMax() {
        return (double) getStateHelper().eval(PropertyKeys.max, 4.0);
    }

    public void setMax(final double max) {
        getStateHelper().put(PropertyKeys.max, max);
    }
}
