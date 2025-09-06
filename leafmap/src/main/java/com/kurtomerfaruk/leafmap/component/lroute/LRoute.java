package com.kurtomerfaruk.leafmap.component.lroute;

import com.kurtomerfaruk.leafmap.utils.Constants;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponentBase;
import org.primefaces.model.map.Point;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.09.2025 00:56
 */
@ResourceDependency(library = Constants.LIBRARY, name = "routemachine/leaflet-routing-machine.css")
@ResourceDependency(library = Constants.LIBRARY, name = "routemachine/leaflet-routing-machine.js")
@ResourceDependency(library = Constants.LIBRARY, name = "routemachine/Control.Geocoder.js")
@ResourceDependency(library = Constants.LIBRARY, name = "routemachine/config.js")
public class LRoute extends UIComponentBase {

    protected enum PropertyKeys {

        points,routeWhileDragging,reverseWaypoints,showAlternatives,draggableWaypoints
    }

    public LRoute() {
        setRendererType(null);
    }

    @Override
    public String getFamily() {
        return Constants.COMPONENT_FAMILY;
    }

    public List<Point> getPoints() {
        return (List<Point>) getStateHelper().eval(PropertyKeys.points);
    }

    public void setPoints(final List<Point> points) {
        getStateHelper().put(PropertyKeys.points, points);
    }

    public boolean isRouteWhileDragging() {
        return (Boolean) getStateHelper().eval(PropertyKeys.routeWhileDragging, false);
    }

    public void setRouteWhileDragging(boolean routeWhileDragging) {
        getStateHelper().put(PropertyKeys.routeWhileDragging, routeWhileDragging);
    }
    public boolean isReverseWaypoints() {
        return (Boolean) getStateHelper().eval(PropertyKeys.reverseWaypoints, false);
    }

    public void setReverseWaypoints(boolean reverseWaypoints) {
        getStateHelper().put(PropertyKeys.reverseWaypoints, reverseWaypoints);
    }

    public boolean isShowAlternatives() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showAlternatives, false);
    }

    public void setShowAlternatives(boolean showAlternatives) {
        getStateHelper().put(PropertyKeys.showAlternatives, showAlternatives);
    }

    public boolean isDraggableWaypoints() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggableWaypoints, false);
    }

    public void setDraggableWaypoints(boolean draggableWaypoints) {
        getStateHelper().put(PropertyKeys.draggableWaypoints, draggableWaypoints);
    }
}
