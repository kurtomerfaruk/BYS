package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import com.google.gson.Gson;
import com.kurtomerfaruk.amchartfaces.model.ChartData;
import com.kurtomerfaruk.amchartfaces.model.ChartModel;
import com.kurtomerfaruk.amchartfaces.model.DefaultChartModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.model.map.LatLng;
import tr.bel.gaziantep.bysweb.core.dtos.GrafikDataDTO;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlCinsiyet;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracCihazTeslimiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracTamirService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGrafik;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGrafikService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Inject
    private SyGrafikService syGrafikService;

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
    @Getter
    @Setter
    private List<GrafikDataDTO> grafikDataList;

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
        if (grafikDataList == null) grafikDataList = new ArrayList<>();
        getAllGraphics();
        getInfoDisabledRequests();
    }

    private void getInfoDisabledRequests() {
        List<EyTalep> taleps = eyTalepService.findByDurum(EnumGnlTalepDurumu.BEKLIYOR);
        if (!taleps.isEmpty()) {
            StringBuilder messageStr = new StringBuilder("Sistemde bekleyen toplam talep sayısı : " + taleps.size() + "\n\n");
            messageStr.append(DateUtil.localdateTimeToString(taleps.get(0).getTarih(), "dd.MM.yyyy")).append(" tarihinden beri bekleyen talebiniz bulunmaktadır.\n\n");
            List<Map.Entry<EyTalepKonu, Long>> ilk10 = taleps.stream()
                    .filter(t -> t.getEyTalepKonu() != null)
                    .collect(Collectors.groupingBy(
                            EyTalep::getEyTalepKonu,
                            Collectors.counting()))
                    .entrySet()
                    .stream()
                    .sorted(Map.Entry.<EyTalepKonu, Long>comparingByValue().reversed())
                    .limit(10)
                    .toList();
            messageStr.append("Bekleyen taleplerin ilk 10 konusu aşağıda listelenmiştir.\n\n");
            for (Map.Entry<EyTalepKonu, Long> item : ilk10) {
                String format = "Konu : %-" + (32) + "s Sayı : %d%n";
                messageStr.append(String.format(
                        format,
                        item.getKey().getTanim(),
                        item.getValue()
                ));
                messageStr.append("\n");
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Engelsizler Talep Uyarı", messageStr.toString());
            PrimeFaces.current().dialog().showMessageDynamic(message);
        }
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
            if (coordinate.equals("null,null")) continue;
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
        heatmapModels = new ArrayList<>();
        for (String coordinate : coordinates) {
            String[] parts = coordinate.split("\\s*,\\s*");
            LatLng heatmapModel = new LatLng(Double.parseDouble(parts[1]), Double.parseDouble(parts[0]));
            heatmapModels.add(heatmapModel);
        }

        //PrimeFaces.current().ajax().update("mapGroup");
//
//        String dataJson = gson.toJson(heatmapModels, new TypeToken<ArrayList<HeatmapModel>>() {
//        }.getType());
//        ;
//        String newScript = "refreshHeatMap(" + heatmapModels.size() + "," + dataJson + ")";
//        PrimeFaces.current().executeScript(newScript);

    }

    private void getAllGraphics() {
        try {
            List<SyGrafik> grafikler = syGrafikService.findByModul(EnumModul.ENGELSIZLER);

            grafikler.forEach(grafik -> {
                List<ChartData> sonuclar = syGrafikService.executeQuery(grafik.getSorgu());
                GrafikDataDTO grafikData = new GrafikDataDTO();
                ChartModel chartModel = new DefaultChartModel();
                sonuclar.forEach(chartModel::addData);
                grafikData.setChartModel(chartModel);
                grafikData.setSyGrafik(grafik);
                grafikDataList.add(grafikData);
            });


        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
