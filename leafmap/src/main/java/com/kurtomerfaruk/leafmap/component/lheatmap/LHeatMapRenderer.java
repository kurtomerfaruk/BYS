package com.kurtomerfaruk.leafmap.component.lheatmap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kurtomerfaruk.leafmap.component.lmap.LMap;
import com.kurtomerfaruk.leafmap.model.heatmap.HeatmapModel;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import org.primefaces.renderkit.CoreRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.09.2025 21:49
 */
public class LHeatMapRenderer extends CoreRenderer {
    @Override
    public void decode(FacesContext context, UIComponent component) {
        decodeBehaviors(context, component);
    }

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        LMap map = (LMap) component.getParent();
        LHeatMap heatMap =(LHeatMap) component;

        encodeScript(facesContext,map, heatMap);
    }

    protected void encodeScript(FacesContext context, LMap map, LHeatMap heatMap) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<HeatmapModel> models = heatMap.getModels();
        Gson gson = new Gson();
        String dataJson = gson.toJson(
                models,
                new TypeToken<ArrayList<HeatmapModel>>() {}.getType());

        if (heatMap.getModels() != null) {
            writer.startElement("script", heatMap);
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
