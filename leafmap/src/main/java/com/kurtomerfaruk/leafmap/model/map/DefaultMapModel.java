package com.kurtomerfaruk.leafmap.model.map;

import org.primefaces.model.map.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 23:08
 */
public class DefaultMapModel<T> implements MapModel<T>, Serializable {

    @Serial
    private static final long serialVersionUID = -7386700633246300627L;

    private static final Logger LOGGER = Logger.getLogger(DefaultMapModel.class.getName());

    private static final String MARKER_ID_PREFIX = "marker";

    private static final String POLYLINE_ID_PREFIX = "polyline_";

    private static final String POLYGON_ID_PREFIX = "polygon_";

    private static final String CIRCLE_ID_PREFIX = "circle_";

    private static final String RECTANGLE_ID_PREFIX = "rectangle_";

    private static final String ROUTE_ID_PREFIX = "route_";

    private List<Marker<T>> markers;
    private List<Polyline<T>> polylines;
    private List<Polygon<T>> polygons;
    private List<Circle<T>> circles;
    private List<Rectangle<T>> rectangles;
    private List<Route<T>> routes;

    public DefaultMapModel() {
        markers = new ArrayList<>();
        polylines = new ArrayList<>();
        polygons = new ArrayList<>();
        circles = new ArrayList<>();
        rectangles = new ArrayList<>();
        routes = new ArrayList<>();
    }

    @Override
    public List<Marker<T>> getMarkers() {
        return markers;
    }

    @Override
    public List<Polyline<T>> getPolylines() {
        return polylines;
    }

    @Override
    public List<Polygon<T>> getPolygons() {
        return polygons;
    }

    @Override
    public List<Circle<T>> getCircles() {
        return circles;
    }

    @Override
    public List<Rectangle<T>> getRectangles() {
        return rectangles;
    }

    @Override
    public List<Route<T>> getRoutes() {
        return routes;
    }

    @Override
    public void addOverlay(Overlay<T> overlay) {
        if (overlay.getId() != null) {
            LOGGER.log(Level.WARNING, "Overlays should not have a set ID. It will be overwritten.");
        }
        if (overlay instanceof Marker) {
            overlay.setId(MARKER_ID_PREFIX + UUID.randomUUID());
            markers.add((Marker<T>) overlay);
        }
        else if (overlay instanceof Polyline) {
            overlay.setId(POLYLINE_ID_PREFIX + UUID.randomUUID());
            polylines.add((Polyline<T>) overlay);
        }
        else if (overlay instanceof Polygon) {
            overlay.setId(POLYGON_ID_PREFIX + UUID.randomUUID());
            polygons.add((Polygon<T>) overlay);
        }
        else if (overlay instanceof Circle) {
            overlay.setId(CIRCLE_ID_PREFIX + UUID.randomUUID());
            circles.add((Circle<T>) overlay);
        }
        else if (overlay instanceof Rectangle) {
            overlay.setId(RECTANGLE_ID_PREFIX + UUID.randomUUID());
            rectangles.add((Rectangle<T>) overlay);
        }
        else if (overlay instanceof Route){
            overlay.setId(ROUTE_ID_PREFIX + UUID.randomUUID());
            routes.add((Route<T>) overlay);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Overlay<T> findOverlay(String id) {
        List<? extends Overlay<?>> overlays = Collections.emptyList();

        if (id.startsWith(MARKER_ID_PREFIX)) {
            overlays = markers;
        }
        else if (id.startsWith(POLYLINE_ID_PREFIX)) {
            overlays = polylines;
        }
        else if (id.startsWith(POLYGON_ID_PREFIX)) {
            overlays = polygons;
        }
        else if (id.startsWith(CIRCLE_ID_PREFIX)) {
            overlays = circles;
        }
        else if (id.startsWith(RECTANGLE_ID_PREFIX)) {
            overlays = rectangles;
        }
        else if (id.startsWith(ROUTE_ID_PREFIX)) {
            overlays = routes;
        }

        for (Overlay<?> overlay : overlays) {
            if (overlay.getId().equals(id)) {
                return (Overlay<T>) overlay;
            }
        }

        return null;
    }
}
