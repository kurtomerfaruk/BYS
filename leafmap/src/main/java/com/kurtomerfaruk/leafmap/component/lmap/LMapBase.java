package com.kurtomerfaruk.leafmap.component.lmap;

import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.behavior.ClientBehaviorHolder;
import org.primefaces.component.api.PrimeClientBehaviorHolder;
import org.primefaces.component.api.Widget;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 13:35
 */
public abstract class LMapBase extends UIComponentBase implements Widget, ClientBehaviorHolder, PrimeClientBehaviorHolder {
    public static final String COMPONENT_FAMILY = "com.kurtomerfaruk.leafmap.component";

    public static final String DEFAULT_RENDERER = "com.kurtomerfaruk.leafmap.component.LMapRenderer";

    public enum PropertyKeys {

        widgetVar, model, style, styleClass, center, zoom, zoomControl, attribution, tileUrl, draggable, onPointClick,
        scrollWheel, loadingControl, fullScreen
    }

    public LMapBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public com.kurtomerfaruk.leafmap.model.map.MapModel getModel() {
        return (com.kurtomerfaruk.leafmap.model.map.MapModel) getStateHelper().eval(PropertyKeys.model, null);
    }

    public void setModel(com.kurtomerfaruk.leafmap.model.map.MapModel model) {
        getStateHelper().put(PropertyKeys.model, model);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style, null);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getCenter() {
        return (String) getStateHelper().eval(PropertyKeys.center, null);
    }

    public void setCenter(String center) {
        getStateHelper().put(PropertyKeys.center, center);
    }

    public int getZoom() {
        return (Integer) getStateHelper().eval(PropertyKeys.zoom, 8);
    }

    public void setZoom(int zoom) {
        getStateHelper().put(PropertyKeys.zoom, zoom);
    }

    public boolean isZoomControl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.zoomControl, true);
    }

    public void setZoomControl(boolean zoomControl) {
        getStateHelper().put(PropertyKeys.zoomControl, zoomControl);
    }

    public String getAttribution() {
        return (String) getStateHelper().eval(PropertyKeys.attribution, null);
    }

    public void setAttribution(String attribution) {
        getStateHelper().put(PropertyKeys.attribution, attribution);
    }

    public String getTileUrl() {
        return (String) getStateHelper().eval(PropertyKeys.tileUrl, null);
    }

    public void setTileUrl(String tileUrl) {
        getStateHelper().put(PropertyKeys.tileUrl, tileUrl);
    }

    public boolean isDraggable() {
        return (Boolean) getStateHelper().eval(PropertyKeys.draggable, true);
    }

    public void setDraggable(boolean draggable) {
        getStateHelper().put(PropertyKeys.draggable, draggable);
    }

    public String getOnPointClick() {
        return (String) getStateHelper().eval(PropertyKeys.onPointClick, null);
    }

    public void setOnPointClick(String onPointClick) {
        getStateHelper().put(PropertyKeys.onPointClick, onPointClick);
    }

    public boolean isScrollWheel() {
        return (Boolean) getStateHelper().eval(PropertyKeys.scrollWheel, true);
    }

    public void setScrollWheel(boolean scrollWheel) {
        getStateHelper().put(PropertyKeys.scrollWheel, scrollWheel);
    }

    public boolean isLoadingControl() {
        return (Boolean) getStateHelper().eval(PropertyKeys.loadingControl, true);
    }

    public void setLoadingControl(boolean loadingControl) {
        getStateHelper().put(PropertyKeys.loadingControl, loadingControl);
    }

    public boolean isFullScreen() {
        return (Boolean) getStateHelper().eval(PropertyKeys.fullScreen, true);
    }

    public void setFullScreen(boolean fullScreen) {
        getStateHelper().put(PropertyKeys.fullScreen, fullScreen);
    }

}