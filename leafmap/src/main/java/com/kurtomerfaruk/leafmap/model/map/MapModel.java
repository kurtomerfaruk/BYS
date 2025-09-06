package com.kurtomerfaruk.leafmap.model.map;


import org.primefaces.model.map.*;

import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 5.09.2025 23:02
 */
public interface MapModel<T>  {
    void addOverlay(Overlay<T> overlay);

    List<Marker<T>> getMarkers();

    List<Polyline<T>> getPolylines();

    List<Polygon<T>> getPolygons();

    List<Circle<T>> getCircles();

    List<Rectangle<T>> getRectangles();

    List<Route<T>> getRoutes();

    Overlay<T> findOverlay(String id);

}
