package com.kurtomerfaruk.leafmap.component.lmap;

import jakarta.faces.FacesException;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;
import jakarta.faces.event.FacesEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.Marker;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.MapBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 13:35
 */
@ResourceDependency(library = "leafmap", name = "leaflet.css")
@ResourceDependency(library = "leafmap", name = "leaflet.js")
@ResourceDependency(library = "leafmap", name = "loading/loading.css")
@ResourceDependency(library = "leafmap", name = "loading/loading.js")
@ResourceDependency(library = "leafmap", name = "Leaflet.fullscreen.css")
@ResourceDependency(library = "leafmap", name = "Leaflet.fullscreen.min.js")
@ResourceDependency(library = "leafmap", name = "routemachine/leaflet-routing-machine.css")
@ResourceDependency(library = "leafmap", name = "routemachine/leaflet-routing-machine.js")
@ResourceDependency(library = "leafmap", name = "routemachine/Control.Geocoder.js")
@ResourceDependency(library = "leafmap", name = "routemachine/config.js")
@ResourceDependency(library = "leafmap", name = "leafmap.js")
public class LMap extends LMapBase {

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>>builder()
            .put("overlaySelect", OverlaySelectEvent.class)
            .put("overlayDblSelect", OverlaySelectEvent.class)
            .put("stateChange", StateChangeEvent.class)
            .put("pointSelect", PointSelectEvent.class)
            .put("pointDblSelect", PointSelectEvent.class)
            .put("markerDrag", MarkerDragEvent.class)
            .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(org.primefaces.util.Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        String clientId = getClientId(context);

        if (ComponentUtils.isRequestSource(this, context)) {

            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
            FacesEvent wrapperEvent = null;

            if ("overlaySelect".equals(eventName) || "overlayDblSelect".equals(eventName)) {
                wrapperEvent = new OverlaySelectEvent(this, behaviorEvent.getBehavior(), getModel().findOverlay(params.get(clientId + "_overlayId")));
            } else if ("stateChange".equals(eventName)) {
                String[] centerLoc = params.get(clientId + "_center").split(",");
                String[] northeastLoc = params.get(clientId + "_northeast").split(",");
                String[] southwestLoc = params.get(clientId + "_southwest").split(",");
                int zoomLevel = Integer.parseInt(params.get(clientId + "_zoom"));

                LatLng center = new LatLng(Double.parseDouble(centerLoc[0]), Double.parseDouble(centerLoc[1]));
                LatLng northeast = new LatLng(Double.parseDouble(northeastLoc[0]), Double.parseDouble(northeastLoc[1]));
                LatLng southwest = new LatLng(Double.parseDouble(southwestLoc[0]), Double.parseDouble(southwestLoc[1]));

                wrapperEvent = new StateChangeEvent(this, behaviorEvent.getBehavior(), new LatLngBounds(northeast, southwest), zoomLevel, center);
            } else if ("pointSelect".equals(eventName) || "pointDblSelect".equals(eventName)) {
                String[] latlng = params.get(clientId + "_pointLatLng").split(",");
                LatLng position = new LatLng(Double.parseDouble(latlng[0]), Double.parseDouble(latlng[1]));

                wrapperEvent = new PointSelectEvent(this, behaviorEvent.getBehavior(), position);
            } else if ("markerDrag".equals(eventName)) {
                Marker marker = (Marker) getModel().findOverlay(params.get(clientId + "_markerId"));
                double lat = Double.parseDouble(params.get(clientId + "_lat"));
                double lng = Double.parseDouble(params.get(clientId + "_lng"));
                marker.setLatlng(new LatLng(lat, lng));

                wrapperEvent = new MarkerDragEvent(this, behaviorEvent.getBehavior(), marker);
            }

            if (wrapperEvent == null) {
                throw new FacesException("Component " + getClass().getName() + " does not support event " + eventName + "!");
            }

            wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

            super.queueEvent(wrapperEvent);
        } else {
            super.queueEvent(event);
        }
    }
}