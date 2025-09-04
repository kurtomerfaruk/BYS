package com.kurtomerfaruk.leafmap.renderer;

import com.kurtomerfaruk.leafmap.component.LMap;
import com.kurtomerfaruk.leafmap.component.LRoute;
import com.kurtomerfaruk.leafmap.model.Point;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.application.ResourceDependencies;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 15:04
 */
@FacesRenderer(componentFamily = LeafMap.COMPONENT_FAMILY, rendererType = LeafMap.RENDERER_FAMILY)
@ResourceDependencies({
        @ResourceDependency(library = "leafmap", name = "routemachine/leaflet-routing-machine.css"),
        @ResourceDependency(library = "leafmap", name = "routemachine/leaflet-routing-machine.js"),
        @ResourceDependency(library = "leafmap", name = "routemachine/Control.Geocoder.js"),
        @ResourceDependency(library = "leafmap", name = "routemachine/config.js")
})
public class LRouteRenderer extends Renderer {
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        LRoute route = (LRoute) component;
        ResponseWriter writer = context.getResponseWriter();
        LMap map = (LMap) component.getParent();

        while (map == null) {
            map = (LMap) component.getParent();
        }

        String points = pointsToString(route.getPoints());

        writer.startElement("script", component);
        writer.writeText("var control = L.Routing.control({\n" +
                "\twaypoints: "+points +"," +
                "    language: 'tr',\n" +
                "    routeWhileDragging: true,\n" +
                "    reverseWaypoints: false,\n" +
                "    showAlternatives: false,\n" +
                "    altLineOptions: {\n" +
                "        styles: [\n" +
                "            {color: 'black', opacity: 0.15, weight: 9},\n" +
                "            {color: 'white', opacity: 0.8, weight: 6},\n" +
                "            {color: 'blue', opacity: 0.5, weight: 2}\n" +
                "        ]\n" +
                "    }\n" +
                "}).addTo("+map.getWidgetVar()+");", null);


        writer.endElement("script");
    }

    private String pointsToString(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return "[]";
        }

        String result = points.stream()
                .map(p -> "L.latLng(" + p.getLat() + ", " + p.getLng() + ")")
                .collect(Collectors.joining(","));

        return "[" + result + "]";
    }
}
