package com.kurtomerfaruk.leafmap.component.lheatmap;

import com.kurtomerfaruk.leafmap.model.heatmap.HeatmapModel;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:50
 */
public abstract class LHeatMapBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {

    public static final String COMPONENT_FAMILY = "com.kurtomerfaruk.leafmap.component.lheatmap";

    public static final String DEFAULT_RENDERER = "com.kurtomerfaruk.leafmap.component.lheatmap.LHeatMapRenderer";

    protected enum PropertyKeys {

        points, blur, radius, maxZoom, max,models
    }

    public LHeatMapBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List<HeatmapModel> getModels(){
        return (List<HeatmapModel>) getStateHelper().eval(PropertyKeys.models);
    }
    public void setModels(final List<HeatmapModel> models){
        getStateHelper().put(PropertyKeys.models,models);
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
