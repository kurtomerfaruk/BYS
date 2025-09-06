package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.map.LatLng;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlCinsiyet;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracCihazTeslimiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracTamirService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.08.2025 10:40
 */
@Named
@ViewScoped
@Slf4j
public class EngelsizDashboardController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1541720949136400365L;

    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private EyAracTamirService eyAracTamirService;
    @Inject
    private EyAracCihazTeslimiService eyAracCihazTeslimiService;
    @Inject
    private EyTalepService eyTalepService;

    @Getter
    @Setter
    private int totalPerson;
    @Getter
    @Setter
    private int deadPerson;
    @Getter
    @Setter
    private int activePerson;
    @Getter
    @Setter
    private int repairedVehicle;
    @Getter
    @Setter
    private int deliveredVehicle;
    @Getter
    @Setter
    private int request;
    @Getter
    @Setter
    private List<LatLng> heatmapModels;
//    @Getter
//    @Setter
//    private List<ClusterModel> clusterModels;
    @Getter
    @Setter
    private EnumGnlCinsiyet cinsiyet;
    @Getter
    @Setter
    private int yasBaslangic = 0;
    @Getter
    @Setter
    private int yasBitis = 100;
    @Getter
    @Setter
    private int engelOraniBaslangic = 0;
    @Getter
    @Setter
    private int engelOraniBitis = 100;
    @Getter
    @Setter
    private GnlIlce gnlIlce;
    @Getter
    @Setter
    private GnlMahalle gnlMahalle;
    @Getter
    @Setter
    private EyEngelGrubu eyEngelGrubu;
    private Gson gson;

    @PostConstruct
    public void init() {
        heatmapModels = new ArrayList<>();
//        clusterModels = new ArrayList<>();
        gson = new Gson();
        getAddress();
        totalPerson = getTotalCount();
        deadPerson = getTotalDeadCount();
        activePerson = getTotalActiveCount();
        repairedVehicle = getTotalRepairedVehicle();
        deliveredVehicle = getTotalDeliveredVehicle();
        request = getTotalRequest();
    }

    private int getTotalCount() {
        return eyKisiService.count();
    }

    private int getTotalDeadCount() {
        return eyKisiService.getTotalDeadCount();
    }

    private int getTotalActiveCount() {
        return eyKisiService.getTotalActiveCount();
    }

    private int getTotalRepairedVehicle() {
        return eyAracTamirService.count();
    }

    private int getTotalDeliveredVehicle() {
        return eyAracCihazTeslimiService.count();
    }

    private int getTotalRequest() {
        return eyTalepService.count();
    }

    private void getAddress() {
        List<String> coordinates = eyKisiService.getAllCoordinates();
        int count = 0;
        for (String coordinate : coordinates) {
            if(coordinate.equals("null,null")) continue;
            String[] parts = coordinate.split("\\s*,\\s*");
            LatLng heatmapModel = new LatLng(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]));
            heatmapModels.add(heatmapModel);
//            if (count < 100) {
//                ClusterModel clusterModel = new ClusterModel(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]), coordinate);
//                clusterModels.add(clusterModel);
//            }
            count++;
        }
    }

    public void getReport() {
        List<String> coordinates = eyKisiService.getAllCoordinates(cinsiyet, yasBaslangic, yasBitis, gnlIlce, gnlMahalle, engelOraniBaslangic, engelOraniBitis,
                eyEngelGrubu);
//        heatmapModels = new ArrayList<>();
//        for (String coordinate : coordinates) {
//            String[] parts = coordinate.split("\\s*,\\s*");
//            HeatmapModel heatmapModel = new HeatmapModel(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]), 1);
//            heatmapModels.add(heatmapModel);
//        }
//
//        String dataJson = gson.toJson(heatmapModels, new TypeToken<ArrayList<HeatmapModel>>() {
//        }.getType());
//        ;
//        String newScript = "refreshHeatMap(" + heatmapModels.size() + "," + dataJson + ")";
//        PrimeFaces.current().executeScript(newScript);

    }

}
