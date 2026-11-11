package tr.bel.gaziantep.bysweb.moduls.ileriyas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractDashboardController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyTalep;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.service.IyDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.28.0
 * @since 11.11.2026 09:47
 */
@Named
@ViewScoped
@Slf4j
public class IyDashboardController extends AbstractDashboardController {

    @Serial
    private static final long serialVersionUID = -3527683026959023229L;

    @Inject
    private IyDashboardService iyDashboardService;

    @Getter
    @Setter
    private long toplamKisi;
    @Getter
    @Setter
    private long toplamTalep;
    @Getter
    @Setter
    private long bekleyenTalep;
    @Getter
    @Setter
    private long bugunTalep;
    @Getter
    @Setter
    private long buAyTalep;
    @Getter
    @Setter
    private long toplamTalepKonu;

    @Getter
    @Setter
    private List<IyKisi> sonKisiler = new ArrayList<>();
    @Getter
    @Setter
    private List<IyTalep> sonTalepler = new ArrayList<>();
    @Getter
    @Setter
    private List<IyTalep> bekleyenTalepler = new ArrayList<>();

    @Getter
    @Setter
    private String aylikTalepTrendJson = "";
    @Getter
    @Setter
    private String talepKonuDagilimJson = "";
    @Getter
    @Setter
    private String talepDurumuDagilimJson = "";
    @Getter
    @Setter
    private String cinsiyetDagilimJson = "";
    @Getter
    @Setter
    private String yasDagilimiJson = "";
    @Getter
    @Setter
    private String ilceDagilimJson = "";

    @Getter
    @Setter
    private boolean yetkili = true;
    @Getter
    @Setter
    private String hataMesaji;

    @PostConstruct
    public void init() {
        try {
            iyDashboardService.checkDashboardPermission();

            toplamKisi = iyDashboardService.toplamKisiSayisi();
            toplamTalep = iyDashboardService.toplamTalepSayisi();
            bekleyenTalep = iyDashboardService.bekleyenTalepSayisi();
            bugunTalep = iyDashboardService.bugunTalepSayisi();
            buAyTalep = iyDashboardService.buAyTalepSayisi();
            toplamTalepKonu = iyDashboardService.toplamTalepKonuSayisi();

            sonKisiler = iyDashboardService.sonKisiler();
            sonTalepler = iyDashboardService.sonTalepler();
            bekleyenTalepler = iyDashboardService.bekleyenTalepler();

            aylikTalepTrendJson = createMonthlyTrend(iyDashboardService.aylikTalepTrendi(), "Talep");
            talepKonuDagilimJson = createDistribution(iyDashboardService.talepKonuDagilimi(), "Talep Konusu", "doughnut");
            talepDurumuDagilimJson = createDistribution(iyDashboardService.talepDurumuDagilimi(), "Talep Durumu", "pie");
            cinsiyetDagilimJson = createDistribution(iyDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = createDistribution(iyDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = createDistribution(iyDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("İleri Yaş dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("İleri Yaş dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }
}