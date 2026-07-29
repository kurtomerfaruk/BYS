package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import com.kurtomerfaruk.amchartfaces.model.ChartData;
import com.kurtomerfaruk.amchartfaces.model.ChartModel;
import com.kurtomerfaruk.amchartfaces.model.DefaultChartModel;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.dto.MahalleKartiDTO;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.MahalleKartiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.17.0
 * @since 27.07.2026 13:19
 */
@Named
@ViewScoped
@Slf4j
public class MahalleKartiController implements Serializable {
    @Serial
    private static final long serialVersionUID = -702855281222159959L;

    @Inject
    private MahalleKartiService mahalleKartiService;

    @Getter
    @Setter
    private GnlIlce gnlIlce;
    @Getter
    @Setter
    private GnlMahalle gnlMahalle;

    @Getter
    @Setter
    private MahalleKartiDTO mahalleKartiDTO;

    @Getter
    @Setter
    private boolean yuklendi;

    @Getter
    @Setter
    private ChartModel cinsiyetPieModel;

    @Getter
    @Setter
    private ChartModel yasBarModel;
    @Getter
    @Setter
    private boolean yasVar;

    @Getter
    @Setter
    private ChartModel engelOraniPieModel;
    @Getter
    @Setter
    private boolean engelOraniVar;

    @Getter
    @Setter
    private ChartModel engelGrubuPieModel;
    @Getter
    @Setter
    private boolean engelGrubuVar;

    @Getter
    @Setter
    private ChartModel cihazBarModel;
    @Getter
    @Setter
    private boolean cihazVar;

    @Getter
    @Setter
    private ChartModel egitimPieModel;
    @Getter
    @Setter
    private boolean egitimVar;

    @Getter
    @Setter
    private ChartModel medeniPieModel;
    @Getter
    @Setter
    private boolean medeniVar;

    @Getter
    @Setter
    private ChartModel tamirBarModel;
    @Getter
    @Setter
    private boolean tamirVar;

    @Getter
    @Setter
    private ChartModel talepPieModel;
    @Getter
    @Setter
    private boolean talepVar;

    @PostConstruct
    public void init() {
        yuklendi = false;
    }

    public void mahalleKartiGetir() {
        if (gnlMahalle == null) {
            yuklendi = false;
            return;
        }
        try {
            mahalleKartiDTO = mahalleKartiService.getMahalleKarti(gnlMahalle);
            buildCinsiyetPieModel();
            buildYasBarModel();
            buildEngelOraniPieModel();
            buildEngelGrubuPieModel();
            buildCihazBarModel();
            buildEgitimPieModel();
            buildMedeniPieModel();
            buildTamirBarModel();
            buildTalepPieModel();
            yuklendi = true;
        } catch (Exception e) {
            log.error("Mahalle karti yuklenirken hata: ", e);
            yuklendi = false;
        }
    }

    private void buildPieModel(ChartModel[] holder, boolean[] flagHolder, Map<String, Integer> data) {
        holder[0] = new DefaultChartModel();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> holder[0].addData(new ChartData(key, value)));
            flagHolder[0] = true;
        } else {
            flagHolder[0] = false;
        }
    }

    private void buildCinsiyetPieModel() {
        cinsiyetPieModel = new DefaultChartModel();
        Map<String, Integer> data = mahalleKartiDTO.getCinsiyetGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> cinsiyetPieModel.addData(new ChartData(key, value)));
        }
    }

    private void buildYasBarModel() {
        yasBarModel = new DefaultChartModel();
        yasVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getYasDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> yasBarModel.addData(new ChartData(key, value)));
            yasVar = true;
        }
    }

    private void buildEngelOraniPieModel() {
        engelOraniPieModel = new DefaultChartModel();
        engelOraniVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getEngelOraniGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> engelOraniPieModel.addData(new ChartData(key, value)));
            engelOraniVar = true;
        }
    }

    private void buildEngelGrubuPieModel() {
        engelGrubuPieModel = new DefaultChartModel();
        engelGrubuVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getEngelGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> engelGrubuPieModel.addData(new ChartData(key, value)));
            engelGrubuVar = true;
        }
    }

    private void buildCihazBarModel() {
        cihazBarModel = new DefaultChartModel();
        cihazVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getCihazTeslimGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> cihazBarModel.addData(new ChartData(key, value)));
            cihazVar = true;
        }
    }

    private void buildEgitimPieModel() {
        egitimPieModel = new DefaultChartModel();
        egitimVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getEgitimGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> egitimPieModel.addData(new ChartData(key, value)));
            egitimVar = true;
        }
    }

    private void buildMedeniPieModel() {
        medeniPieModel = new DefaultChartModel();
        medeniVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getMedeniGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> medeniPieModel.addData(new ChartData(key, value)));
            medeniVar = true;
        }
    }

    private void buildTamirBarModel() {
        tamirBarModel = new DefaultChartModel();
        tamirVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getTamirGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> tamirBarModel.addData(new ChartData(key, value)));
            tamirVar = true;
        }
    }

    private void buildTalepPieModel() {
        talepPieModel = new DefaultChartModel();
        talepVar = false;
        Map<String, Integer> data = mahalleKartiDTO.getTalepGrubuDagilimi();
        if (data != null && !data.isEmpty()) {
            data.forEach((key, value) -> talepPieModel.addData(new ChartData(key, value)));
            talepVar = true;
        }
    }
}
