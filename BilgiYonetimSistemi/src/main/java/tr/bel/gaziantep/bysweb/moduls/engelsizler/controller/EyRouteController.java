package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Point;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.GeoUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.09.2025 10:34
 */
@Named
@ViewScoped
@Slf4j
public class EyRouteController implements Serializable {
    @Serial
    private static final long serialVersionUID = 6512723075883578836L;

    @Getter
    @Setter
    private List<EyKisi> eyKisiList = new ArrayList<>();
    @Getter
    @Setter
    private EyKisi selectedEyKisi = new EyKisi();
    @Getter
    @Setter
    private List<Point> points=new ArrayList<>();
    @Getter
    @Setter
    private List<Point> markerModels = new ArrayList<>();

    public void addToList() {
        if (!eyKisiList.contains(selectedEyKisi)) {
            eyKisiList.add(selectedEyKisi);
            selectedEyKisi = new EyKisi();
        }
    }

    public void removeFromList(EyKisi eyKisi) {
        eyKisiList.remove(eyKisi);
        FacesUtil.successMessage("satirSilindi");
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        selectedEyKisi = event.getObject();
    }

    public void createRoute(){
        try {
            points = new ArrayList<>();
            markerModels = new ArrayList<>();
            for (EyKisi eyKisi : eyKisiList) {
                if(StringUtil.isNotBlank(eyKisi.getGnlKisi().getLatLng())){
                    String[] parts = eyKisi.getGnlKisi().getLatLng().split(",");
                    Point point = new Point(Double.parseDouble(parts[1]),Double.parseDouble(parts[0]));
                    points.add(point);
                    markerModels.add(point);
                }
            }
            Point start = new Point( 37.06591759577603, 37.37354309665679);
            List<Point> route = GeoUtil.nearestNeighbor(start, points);
            route = GeoUtil.twoOpt(route);
            points = route;
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    public void onReturn(SelectEvent<Map<String, Object>> event) {
        Map<String, Object> data = event.getObject();
        eyKisiList = (List<EyKisi>) data.get("selectedList");

    }

    public void onPointSelect(PointSelectEvent event) {
       LatLng latLng=event.getLatLng();

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected",
                "Lat:" + latLng.getLat() + ", Lng:" + latLng.getLng()));
    }
}
