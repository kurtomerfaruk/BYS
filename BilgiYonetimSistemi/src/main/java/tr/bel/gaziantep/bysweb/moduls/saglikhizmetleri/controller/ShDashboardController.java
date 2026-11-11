package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractDashboardController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.*;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.28.0
 * @since 11.11.2026 11:23
 */
@Named
@ViewScoped
@Slf4j
public class ShDashboardController extends AbstractDashboardController {

    @Serial
    private static final long serialVersionUID = -8008907163153063248L;

    @Inject
    private ShDashboardService shDashboardService;

    @Getter
    @Setter
    private long toplamDanisan;
    @Getter
    @Setter
    private long bugunYeniKayit;
    @Getter
    @Setter
    private long buAyYeniKayit;
    @Getter
    @Setter
    private long toplamKontrol;
    @Getter
    @Setter
    private long bugunKontrol;
    @Getter
    @Setter
    private long buAyKontrol;
    @Getter
    @Setter
    private long toplamTahlil;
    @Getter
    @Setter
    private long bugunTahlil;
    @Getter
    @Setter
    private long buAyTahlil;
    @Getter
    @Setter
    private long toplamObeziteAnket;
    @Getter
    @Setter
    private long buAyObeziteAnket;
    @Getter
    @Setter
    private long toplamRandevu;
    @Getter
    @Setter
    private long bugunRandevu;
    @Getter
    @Setter
    private long buAyRandevu;

    @Getter
    @Setter
    private List<ShKisi> sonDanisanlar = new ArrayList<>();
    @Getter
    @Setter
    private List<ShKisiKontrol> sonKontroller = new ArrayList<>();
    @Getter
    @Setter
    private List<ShKisiKanTahlilSonuc> sonTahliller = new ArrayList<>();
    @Getter
    @Setter
    private List<ShRandevu> sonRandevular = new ArrayList<>();
    @Getter
    @Setter
    private List<ShObeziteAnket> sonObeziteAnketleri = new ArrayList<>();

    @Getter
    @Setter
    private String aylikDanisanTrendJson = "";
    @Getter
    @Setter
    private String danismanlikHizmetiDagilimJson = "";
    @Getter
    @Setter
    private String verilenHizmetDagilimJson = "";
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
            shDashboardService.checkDashboardPermission();

            toplamDanisan = shDashboardService.toplamDanisanSayisi();
            bugunYeniKayit = shDashboardService.bugunYeniKayitSayisi();
            buAyYeniKayit = shDashboardService.buAyYeniKayitSayisi();
            toplamKontrol = shDashboardService.toplamKontrolSayisi();
            bugunKontrol = shDashboardService.bugunKontrolSayisi();
            buAyKontrol = shDashboardService.buAyKontrolSayisi();
            toplamTahlil = shDashboardService.toplamTahlilSayisi();
            bugunTahlil = shDashboardService.bugunTahlilSayisi();
            buAyTahlil = shDashboardService.buAyTahlilSayisi();
            toplamObeziteAnket = shDashboardService.toplamObeziteAnketSayisi();
            buAyObeziteAnket = shDashboardService.buAyObeziteAnketSayisi();
            toplamRandevu = shDashboardService.toplamRandevuSayisi();
            bugunRandevu = shDashboardService.bugunRandevuSayisi();
            buAyRandevu = shDashboardService.buAyRandevuSayisi();

            sonDanisanlar = shDashboardService.sonDanisanlar();
            sonKontroller = shDashboardService.sonKontroller();
            sonTahliller = shDashboardService.sonTahliller();
            sonRandevular = shDashboardService.sonRandevular();
            sonObeziteAnketleri = shDashboardService.sonObeziteAnketleri();

            aylikDanisanTrendJson = createMonthlyTrend(shDashboardService.aylikDanisanTrendi(), "Yeni Danışan");
            danismanlikHizmetiDagilimJson = createDistribution(shDashboardService.danismanlikHizmetiDagilimi(), "Danışmanlık Hizmeti", "pie");
            verilenHizmetDagilimJson = createDistribution(shDashboardService.verilenHizmetDagilimi(), "Verilen Hizmet", "doughnut");
            cinsiyetDagilimJson = createDistribution(shDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = createDistribution(shDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = createDistribution(shDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Sağlık Hizmetleri dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Sağlık Hizmetleri dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }
}
