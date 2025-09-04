package com.kurtomerfaruk.leafmap.renderer;

import com.kurtomerfaruk.leafmap.component.LMap;
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

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 11:59
 */
@FacesRenderer(componentFamily = LeafMap.COMPONENT_FAMILY, rendererType = LeafMap.RENDERER_FAMILY)
@ResourceDependencies({
        @ResourceDependency(library = "leafmap", name = "leaflet.css"),
        @ResourceDependency(library = "leafmap", name = "leaflet.js"),
        @ResourceDependency(library = "leafmap", name = "loading/loading.css"),
        @ResourceDependency(library = "leafmap", name = "loading/loading.js"),
        @ResourceDependency(library = "leafmap", name = "Leaflet.fullscreen.css"),
        @ResourceDependency(library = "leafmap", name = "Leaflet.fullscreen.min.js"),
        @ResourceDependency(library = "leafmap", name = "leaf-utils.js"),
        @ResourceDependency(library = "leafmap", name = "main.js", target = "body"),
        @ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
})
public class LMapRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LMap map = (LMap) component;
        ResponseWriter writer = context.getResponseWriter();
        String clientId = map.getClientId(context);

        writer.startElement("div", map);
        writer.writeAttribute("id", clientId, null);

        if (map.getStyleClass() != null) {
            writer.writeAttribute("class", map.getStyleClass(), null);
        }

        StringBuilder styleBuilder = new StringBuilder();
        styleBuilder.append("z-index:0;");

        if (map.getWidth() != null && !map.getWidth().isEmpty()) {
            styleBuilder.append("width:").append(map.getWidth()).append(";");
        } else {
            styleBuilder.append("width:100%;");
        }

        if (map.getHeight() != null && !map.getHeight().isEmpty()) {
            styleBuilder.append("height:").append(map.getHeight()).append(";");
        } else {
            styleBuilder.append("height:100%");
        }

        if (map.getStyle() != null) {
            styleBuilder.append(map.getStyle());
        }

        writer.writeAttribute("style", styleBuilder.toString(), null);
        writer.endElement("div");

        String id = map.getId().replace(":", "\\\\:");

        writer.startElement("script", map);
        writer.writeText("var " + map.getWidgetVar() + "=null;", null);
        writer.writeText("(function() {", null);
        writer.writeText("  var initMap = function() {", null);
        writer.writeText("    var mapElement = document.getElementById('" + clientId + "');", null);
        writer.writeText("    if (!mapElement) return;", null);
        writer.writeText("    var parent = mapElement.parentNode;", null);
        writer.writeText("    if (!parent) return;", null);

        writer.writeText("    if (parent.clientHeight <= 0) {", null);
        writer.writeText("      parent.style.height = '100vh';", null);
        writer.writeText("      parent.style.overflow = 'hidden';", null);
        writer.writeText("    }", null);

        writer.writeText("     " + map.getWidgetVar() + " = L.map(mapElement,{" +
                "loadingControl:" +map.getLoadingControl() +
                "}).setView([" +
                map.getCenter() + "], " +
                map.getZoom() + ");", null);
        writer.writeText(map.getWidgetVar() + ".invalidateSize();", null);
        if(map.getFullScreen()){
            writer.writeText("var fsControl = L.control.fullscreen();" +
                    map.getWidgetVar()+".addControl(fsControl);",null);
        }


        // Window resize event'i
        writer.writeText("    window.addEventListener('resize', function() {", null);
        writer.writeText(map.getWidgetVar() + ".invalidateSize();", null);
        writer.writeText("    });", null);

        writer.writeText("    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {", null);
        writer.writeText("      attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors'", null);
        writer.writeText("    }).addTo(" + map.getWidgetVar() + ");", null);
        writer.writeText("  };", null);

        List<Point> markers = map.getMarkers();
        if (markers != null) {
            for (Point m : markers) {
                writer.write("L.marker([" + m.getLat() + ", " + m.getLng() + "]).addTo("+map.getWidgetVar()+").bindPopup('" + m.getName() + "');\n");
            }
        }

        // DOM yüklendikten sonra ve AJAX sonrası çalıştır
        writer.writeText("  if (document.readyState !== 'loading') {", null);
        writer.writeText("    initMap();", null);
        writer.writeText("  } else {", null);
        writer.writeText("    document.addEventListener('DOMContentLoaded', initMap);", null);
        writer.writeText("  }", null);

        // JSF AJAX desteği
        writer.writeText("  if (typeof jsf !== 'undefined') {", null);
        writer.writeText("    jsf.ajax.addOnEvent(function(data) {", null);
        writer.writeText("      if (data.status === 'success') {", null);
        writer.writeText("        setTimeout(initMap, 50);", null);
        writer.writeText("      }", null);
        writer.writeText("    });", null);
        writer.writeText("  }", null);
        writer.writeText("})();", null);
        writer.endElement("script");

    }
}
