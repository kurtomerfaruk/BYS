package com.kurtomerfaruk.leafmap.component;

import com.kurtomerfaruk.leafmap.model.Point;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponentBase;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 11:55
 */
public class LMap extends UIComponentBase {

    public enum PropertyKeys {

        widgetVar,
        center,
        zoom,
        styleClass,
        style,
        width,
        height,
        loadingControl,
        fullScreen,
        markers
    }

    @Override
    public String getFamily() {
        return LeafMap.COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getCenter() {
        return (String) getStateHelper().eval(PropertyKeys.center, null);
    }

    public void setCenter(String center) {
        getStateHelper().put(PropertyKeys.center, center);
    }

    public String getZoom() {
        return (String) getStateHelper().eval(PropertyKeys.zoom, null);
    }

    public void setZoom(String zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }
    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getWidth() {
        return (String) getStateHelper().eval(PropertyKeys.width, null);
    }

    public void setWidth(String width) {
        getStateHelper().put(PropertyKeys.width, width);
    }
    public String getHeight() {
        return (String) getStateHelper().eval(PropertyKeys.height, null);
    }

    public void setHeight(String height) {
        getStateHelper().put(PropertyKeys.height, height);
    }

    public boolean getLoadingControl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.loadingControl, true);
    }

    public void setLoadingControl(boolean loadingControl) {
        getStateHelper().put(PropertyKeys.loadingControl, loadingControl);
    }

    public boolean getFullScreen() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fullScreen, true);
    }

    public void setFullScreen(boolean fullScreen) {
        getStateHelper().put(PropertyKeys.fullScreen, fullScreen);
    }

    public List<Point> getMarkers() {
        return (List<Point>) getStateHelper().eval(PropertyKeys.markers);
    }

    public void setMarkers(final List<Point> markers) {
        getStateHelper().put(PropertyKeys.markers, markers);
    }

}