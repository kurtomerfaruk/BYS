package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.model.heatmap.HeatmapModel;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 11:19
 */
public class LHeatMap extends UIComponentBase {

    protected enum PropertyKeys {

        points, blur, radius, maxZoom, max,models
    }

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

    public List<HeatmapModel> getModels(){
        return (List<HeatmapModel>) getStateHelper().eval(PropertyKeys.models);
    }
    public void setModels(final List<HeatmapModel> models){
        getStateHelper().put(PropertyKeys.models,models);
    }

    public List<String> getPoints() {
        return (List<String>) getStateHelper().eval(PropertyKeys.points);
    }

    public void setPoints(final List<String> points) {
        getStateHelper().put(PropertyKeys.points, points);
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
