package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 13:42
 */
public class LMarker extends UIComponentBase {

    public enum PropertyKeys {

        lat,
        lng,
        title,
        draggable,
    }

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

    public Double getLat() {
        return (Double) getStateHelper().eval(PropertyKeys.lat, null);
    }

    public void setLat(Double lat) {
        getStateHelper().put(PropertyKeys.lat, lat);
    }
    public Double getLng() {
        return (Double) getStateHelper().eval(PropertyKeys.lng, null);
    }

    public void setLng(Double lng) {
        getStateHelper().put(PropertyKeys.lng, lng);
    }

    public String getTitle() {
        return (String) getStateHelper().eval(PropertyKeys.title, null);
    }

    public void setTitle(String title) {
        getStateHelper().put(PropertyKeys.title, title);
    }
    public Boolean getDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, null);
    }

    public void setDraggable(String draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }
}
