package com.kurtomerfaruk.leafmap.renderer;

import com.kurtomerfaruk.leafmap.component.LChoropleth;
import com.kurtomerfaruk.leafmap.component.LMap;
import com.kurtomerfaruk.leafmap.utils.LeafFunctions;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.Renderer;

import java.io.IOException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 14:56
 */
public class LChoroplethRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LChoropleth choropleth = (LChoropleth) component;
        ResponseWriter writer = context.getResponseWriter();
        LMap map = (LMap) component.getParent();

        while (map == null) {
            map = (LMap) component.getParent();
        }

        if (choropleth.getData() != null) {
            writer.startElement("script", component);
            writer.writeText("document.addEventListener('DOMContentLoaded', function() {", null);

            writer.writeText("const info = L.control();" +
                    "info.onAdd = function (" + map.getWidgetVar() + ") {" +
                    "   this._div = L.DomUtil.create('div', 'info');" +
                    "   this.update();" +
                    "   return this._div;" +
                    "};", null);

            writer.writeText("info.update = function (props) {" +
                    "   const contents = props ? `<b>${props.name}</b><br />${props.value} " + choropleth.getUnit() + " : '" + choropleth.getInfo() + "';" +
                    "   this._div.innerHTML = `<h4>" + choropleth.getTitle() + "</h4>${contents}`" +
                    "}", null);

            writer.writeText("info.addTo(" + map.getWidgetVar() + ");", null);

            String grades = LeafFunctions.generateGrades(choropleth.getValues());
            writer.writeText("const grades = " + grades + ";", null);

//            writer.writeText("function getColor(d) {" +
//                    "   return d > grades[grades.length-1] ? '#800026' :" +
//                    "       d > grades[grades.length-2]  ? '#BD0026' :" +
//                    "       d > grades[grades.length-3]  ? '#E31A1C' :" +
//                    "       d > grades[grades.length-4]  ? '#FC4E2A' :" +
//                    "       d > grades[grades.length-5]   ? '#FD8D3C' :" +
//                    "       d > grades[grades.length-6]   ? '#FEB24C' :" +
//                    "       d > grades[grades.length-7]   ? '#FED976' : '#FFEDA0';" +
//                    "}",null);

//            writer.writeText("function style(feature) {" +
//                    "   return {" +
//                    "       weight: 2," +
//                    "       opacity: 1," +
//                    "       color: 'white'," +
//                    "       dashArray: '3'," +
//                    "       fillOpacity: 0.7," +
//                    "       fillColor: getColor(feature.properties.value)" +
//                    "   }" +
//                    "}", null);
//
            writer.writeText("function highlightFeature(e) {" +
                    "   const layer = e.target;" +
                    "   layer.setStyle({" +
                    "       weight: 5," +
                    "       color: '#666'," +
                    "       dashArray: ''," +
                    "       fillOpacity: 0.7" +
                    "   });" +
                    "   layer.bringToFront();" +
                    "   info.update(layer.feature.properties);" +
                    "}",null);

            writer.writeText("const geojson = L.geoJson(statesData, {" +
                    "   style," +
                    "   onEachFeature" +
                    " }).addTo("+map.getWidgetVar()+");",null);


            writer.writeText("function resetHighlight(e) {\n" +
                    "\t\tgeojson.resetStyle(e.target);\n" +
                    "\t\tinfo.update();\n" +
                    "\t}",null);

            writer.writeText("function zoomToFeature(e) {\n" +
                    "\t\tmap.fitBounds(e.target.getBounds());\n" +
                    "\t}",null);

            writer.writeText("function onEachFeature(feature, layer) {\n" +
                    "\t\tlayer.on({\n" +
                    "\t\t\tmouseover: highlightFeature,\n" +
                    "\t\t\tmouseout: resetHighlight,\n" +
                    "\t\t\tclick: zoomToFeature\n" +
                    "\t\t});\n" +
                    "\t}\n",null);

            writer.writeText("const legend = L.control({position: 'bottomright'});",null);

            writer.writeText("legend.onAdd = function (map) {" +
                    "   const div = L.DomUtil.create('div', 'info legend');" +
                    "   const grades = [0, 10, 20, 50, 100, 200, 500, 1000];" +
                    "   const labels = [];" +
                    "   let from, to;" +
                    "   for (let i = 0; i < grades.length; i++) {" +
                    "   from = grades[i];" +
                    "   to = grades[i + 1];" +
                    "   labels.push(`<i style=\"background:${getColor(from + 1)}\"></i> ${from}${to ? `&ndash;${to}` : '+'}`);" +
                    "   }" +
                    "   div.innerHTML = labels.join('<br>');" +
                    "   return div;" +
                    "};" +
                    "legend.addTo("+map.getWidgetVar()+");",null);

            writer.writeText("});", null);
            writer.endElement("script");
        }
    }
}
