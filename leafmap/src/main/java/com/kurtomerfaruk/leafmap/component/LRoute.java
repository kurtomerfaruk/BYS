package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.model.Point;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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

    protected enum PropertyKeys {

        points,routeWhileDragging,reverseWaypoints,showAlternatives
    }

    public List<Point> getPoints() {
        return (List<Point>) getStateHelper().eval(PropertyKeys.points);
    }

    public void setPoints(final List<Point> points) {
        getStateHelper().put(PropertyKeys.points, points);
    }

    public boolean getRouteWhileDragging() {
        return (Boolean) getStateHelper().eval(PropertyKeys.routeWhileDragging, true);
    }

    public void setRouteWhileDragging(boolean routeWhileDragging) {
        getStateHelper().put(PropertyKeys.routeWhileDragging, routeWhileDragging);
    }
    public boolean getReverseWaypoints() {
        return (Boolean) getStateHelper().eval(PropertyKeys.reverseWaypoints, false);
    }

    public void setReverseWaypoints(boolean reverseWaypoints) {
        getStateHelper().put(PropertyKeys.reverseWaypoints, reverseWaypoints);
    }
    public boolean getShowAlternatives() {
        return (Boolean) getStateHelper().eval(PropertyKeys.showAlternatives, false);
    }

    public void setShowAlternatives(boolean showAlternatives) {
        getStateHelper().put(PropertyKeys.showAlternatives, showAlternatives);
    }
}
