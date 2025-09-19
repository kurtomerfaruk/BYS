package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.*;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.GeoUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.09.2025 09:08
 */
@Slf4j
public abstract class AbstractRouteController<T> implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 9169883447701018311L;
    @Inject
    protected KpsController kpsController;
    @Inject
    protected GnlKisiService gnlKisiService;

    @Getter
    @Setter
    protected List<T> kisiList = new ArrayList<>();
    @Getter
    @Setter
    protected T selectedKisi;
    @Getter
    @Setter
    protected List<Point> points = new ArrayList<>();
    @Getter
    @Setter
    protected String startPointStr;
    @Getter
    @Setter
    protected Point startPoint;
    @Getter
    @Setter
    protected MapModel<Long> mapModel;

    @PostConstruct
    public void init(){
        mapModel = new DefaultMapModel<>();
        LatLng centerCord = new LatLng(37.06591759577603, 37.37354309665679);
        startPointStr = centerCord.getLat()+","+centerCord.getLng();
        startPoint = new Point(centerCord.getLat(),centerCord.getLng());
        mapModel.addOverlay(new Marker<>(centerCord, "Merkez", 1L));
        mapModel.getMarkers().get(0).setDraggable(true);
    }

    public void removeFromList(T kisi) {
        kisiList.remove(kisi);
        FacesUtil.successMessage("satirSilindi");
    }

    public void clearList() {
        kisiList = new ArrayList<>();
    }

    public void secilenKisi(SelectEvent<T> event) {
        selectedKisi = event.getObject();
    }

    public void createRoute() {
        try {
            if(startPoint == null){
                FacesUtil.addExclamationMessage("Başlangıç noktasını seçmeden rota hesaplaması yapılamaz");
                return;
            }
            points = new ArrayList<>();
            for (T kisi : kisiList) {
                String latLng = getLatLng(kisi);
                if (StringUtil.isNotBlank(latLng)) {
                    String[] parts = latLng.split(",");
                    Point point = new Point(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]));
                    points.add(point);
                }
            }
            List<Point> route = GeoUtil.nearestNeighbor(startPoint, points);
            route = GeoUtil.twoOpt(route);
            points = route;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void onReturn(SelectEvent<Map<String, Object>> event) {
        Map<String, Object> data = event.getObject();
        List<T> selectedList = (List<T>) data.get("selectedList");
        if (selectedList != null) {
            for (T item : selectedList) {
                if (!kisiList.contains(item)) {
                    kisiList.add(item);
                }
            }
        }
    }

    public void onPointSelect(PointSelectEvent event) {
        LatLng latLng = event.getLatLng();
        startPoint = new Point(latLng.getLat(), latLng.getLng());
        startPointStr = latLng.getLat() + "," + latLng.getLng();
    }

    public void onMarkerDrag(MarkerDragEvent<Long> event) {
        Marker<Long> marker = event.getMarker();
        startPoint = new Point(marker.getLatlng().getLat(), marker.getLatlng().getLng());
        startPointStr = marker.getLatlng().getLat() + "," + marker.getLatlng().getLng();
    }

    public void getTcKimlik(T kisi) {
        try {
            if (kisi != null) {
                String tcKimlikNo = getTcKimlikNo(kisi);
                GnlKisi gnlKisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (gnlKisi == null) gnlKisi = getGnlKisi(kisi);
                gnlKisi = kpsController.findByTcKimlikNo(gnlKisi, getModul());
                if (gnlKisi != null) {
                    setGnlKisi(kisi, gnlKisi);
                    if (gnlKisi.getDurum() == EnumGnlDurum.SAG) {
                        int index = kisiList.indexOf(kisi);
                        kisiList.set(index, kisi);
                    } else {
                        kisiList.remove(kisi);
                    }
                    saveEntity(kisi);
                }
            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }

    // --- Abstract metodlar (alt sınıflar override edecek) ---
    protected abstract String getLatLng(T kisi);
    protected abstract String getTcKimlikNo(T kisi);
    protected abstract GnlKisi getGnlKisi(T kisi);
    protected abstract void setGnlKisi(T kisi, GnlKisi gnlKisi);
    protected abstract EnumModul getModul();
    protected abstract void saveEntity(T kisi);
}
