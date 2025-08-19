package com.kurtomerfaruk.leafmap.renderer;

import com.kurtomerfaruk.leafmap.component.LMap;
import com.kurtomerfaruk.leafmap.component.LMarker;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 13:43
 */
@FacesRenderer(componentFamily = LeafMap.COMPONENT_FAMILY, rendererType = LeafMap.RENDERER_FAMILY)
public class LMarkerRenderer extends Renderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        LMarker marker = (LMarker) component;
        ResponseWriter writer = context.getResponseWriter();
        String mapId = findParentMapId(component);
        LMap map = (LMap) component.getParent();

        while (map == null) {
            map = (LMap) component.getParent();
        }

        if (mapId != null && marker.getLat() != null && marker.getLng() != null) {
            writer.startElement("script", component);
            writer.writeText("document.addEventListener('DOMContentLoaded', function() {", null);
//            writer.writeText("var defaultIcon = L.icon({\n" +
//                    "    iconUrl: 'jakarta.faces.resource/images/marker-icon.png.xhtml?ln=leafmap',\n" +
//                    "    shadowUrl: 'jakarta.faces.resource/images/marker-shadow.png.xhtml?ln=leafmap',\n" +
//                    "    iconSize: [25, 41],\n" +
//                    "    iconAnchor: [12, 41],\n" +
//                    "    popupAnchor: [1, -34],\n" +
//                    "    shadowSize: [41, 41]\n" +
//                    "});",null);
            writer.writeText("var marker = L.marker([" + marker.getLat() + ", " + marker.getLng() + "])", null);

            if (marker.getDraggable() != null) {
                writer.writeText(".setDraggable(" + marker.getDraggable() + ")", null);
            }

//            writer.writeText(".addTo(document.getElementById('" + mapId + "'));", null);
            writer.writeText(".addTo("+map.getWidgetVar()+");", null);

            if (marker.getTitle() != null && !marker.getTitle().isEmpty()) {
                writer.writeText("marker.bindPopup('" + marker.getTitle() + "');", null);
            }

            writer.writeText("});", null);
            writer.endElement("script");
        }
    }

    private String findParentMapId(UIComponent component) {
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent.getClass().getSimpleName().equals("LMap")) {
                return parent.getClientId(FacesContext.getCurrentInstance());
            }
            parent = parent.getParent();
        }
        return null;
    }
}