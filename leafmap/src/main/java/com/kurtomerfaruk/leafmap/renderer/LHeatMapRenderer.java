package com.kurtomerfaruk.leafmap.renderer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kurtomerfaruk.leafmap.component.LHeatMap;
import com.kurtomerfaruk.leafmap.component.LMap;
import com.kurtomerfaruk.leafmap.model.heatmap.HeatmapModel;
import com.kurtomerfaruk.leafmap.utils.LeafMap;
import jakarta.faces.application.ResourceDependencies;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 11:20
 */
@FacesRenderer(componentFamily = LeafMap.COMPONENT_FAMILY, rendererType = LeafMap.RENDERER_FAMILY)
@ResourceDependencies({
        @ResourceDependency(library = "leafmap", name = "heatmap/heatmap.min.js"),
        @ResourceDependency(library = "leafmap", name = "heatmap/leaflet-heatmap.js")
})
public class LHeatMapRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LHeatMap heatMap = (LHeatMap) component;
        ResponseWriter writer = context.getResponseWriter();
        LMap map = (LMap) component.getParent();

        while (map == null) {
            map = (LMap) component.getParent();
        }

        List<HeatmapModel> models = heatMap.getModels();
        Gson gson = new Gson();
        String dataJson = gson.toJson(
                models,
                new TypeToken<ArrayList<HeatmapModel>>() {}.getType());

        if (heatMap.getModels() != null) {
            writer.startElement("script", component);
            String data = "var heatMapData = {\n" +
                    "          max: "+models.size()+",\n" +
                    "data:"+dataJson+
                    "        }; \n" +
                    "var cfg = {\n" +
                    "          \"radius\": 25," +
                    "          \"maxOpacity\": .8," +
                    "          \"scaleRadius\": false," +
                    "          \"useLocalExtrema\": true," +
                    "          latField: 'lat'," +
                    "          lngField: 'lng'," +
                    "          valueField: 'count'" +
                    "        };\n" +
                    "        var heatmapLayer = new HeatmapOverlay(cfg);\n" +
                    "setTimeout(function(){\n" +
                    map.getWidgetVar() + ".addLayer(heatmapLayer);\n" +
                    "heatmapLayer.setData(heatMapData);" +
                    "},500);" ;

            writer.writeText(data,null);

                writer.endElement("script");
        }
    }
}
