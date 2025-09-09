package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.*;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.GeoUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

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

    @Inject
    private KpsController kpsController;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private EyKisiService eyKisiService;

    @Getter
    @Setter
    private List<EyKisi> eyKisiList = new ArrayList<>();
    @Getter
    @Setter
    private EyKisi selectedEyKisi = new EyKisi();
    @Getter
    @Setter
    private List<Point> points = new ArrayList<>();
    //    @Getter
//    @Setter
//    private List<Point> markerModels = new ArrayList<>();
    @Getter
    @Setter
    private String startPointStr;
    @Getter
    @Setter
    private Point startPoint;
    @Getter
    @Setter
    private MapModel<Long> mapModel;

//    public void addToList() {
//        if (!eyKisiList.contains(selectedEyKisi)) {
//            eyKisiList.add(selectedEyKisi);
//            selectedEyKisi = new EyKisi();
//        }
//    }

    @PostConstruct
    public void init(){
        mapModel = new DefaultMapModel<>();
        LatLng centerCord = new LatLng(37.06591759577603, 37.37354309665679);
        startPointStr = centerCord.getLat()+","+centerCord.getLng();
        mapModel.addOverlay(new Marker<>(centerCord, "Merkez", 1L));

        mapModel.getMarkers().get(0).setDraggable(true);
    }

    public void removeFromList(EyKisi eyKisi) {
        eyKisiList.remove(eyKisi);
        FacesUtil.successMessage("satirSilindi");
    }

    public void clearList() {
        eyKisiList = new ArrayList<>();
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        selectedEyKisi = event.getObject();
    }

    public void createRoute() {
        try {
            if(startPoint == null){
                FacesUtil.warningMessage("Başlangıç noktasını seçmeden rota hesaplaması yapılamaz");
                return;
            }
            points = new ArrayList<>();
//            markerModels = new ArrayList<>();
            for (EyKisi eyKisi : eyKisiList) {
                if (StringUtil.isNotBlank(eyKisi.getGnlKisi().getLatLng())) {
                    String[] parts = eyKisi.getGnlKisi().getLatLng().split(",");
                    Point point = new Point(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]));
                    points.add(point);
//                    markerModels.add(point);
                }
            }
            //Point start = new Point(37.06591759577603, 37.37354309665679);
            List<Point> route = GeoUtil.nearestNeighbor(startPoint, points);
            route = GeoUtil.twoOpt(route);
            points = route;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void onReturn(SelectEvent<Map<String, Object>> event) {
        Map<String, Object> data = event.getObject();
        List<EyKisi> selectedList = (List<EyKisi>) data.get("selectedList");
        if (selectedList != null) {
            for (EyKisi item : selectedList) {
                if (!eyKisiList.contains(item)) {
                    eyKisiList.add(item);
                }
            }
        }
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latLng = event.getLatLng();
        startPoint = new Point(latLng.getLat(), latLng.getLng());
        startPointStr = latLng.getLat() + "," + latLng.getLng();

//        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Point Selected",
//                "Lat:" + latLng.getLat() + ", Lng:" + latLng.getLng()));
    }


    public void onMarkerDrag(MarkerDragEvent<Long> event) {
        Marker<Long> marker = event.getMarker();
        startPoint = new Point(marker.getLatlng().getLat(), marker.getLatlng().getLng());
        startPointStr = marker.getLatlng().getLat() + "," + marker.getLatlng().getLng();
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage(FacesMessage.SEVERITY_INFO,
//                        "Marker " + marker.getData() + " Dragged",
//                        "Lat:" + marker.getLatlng().getLat() + ", Lng:" + marker.getLatlng().getLng()));
    }

    public void getTcKimlik(EyKisi eyKisi) {
        try {
            if (eyKisi != null) {
                String tcKimlikNo = eyKisi.getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = eyKisi.getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.ENGELSIZLER);
                if (kisi != null) {
                    eyKisi.setGnlKisi(kisi);
                    if (kisi.getDurum() == EnumGnlDurum.SAG) {
                        int index = eyKisiList.indexOf(eyKisi);
                        eyKisiList.set(index, eyKisi);
                    } else {
                        eyKisiList.remove(eyKisi);
                    }
                    eyKisiService.edit(eyKisi);

                }

            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }
}
