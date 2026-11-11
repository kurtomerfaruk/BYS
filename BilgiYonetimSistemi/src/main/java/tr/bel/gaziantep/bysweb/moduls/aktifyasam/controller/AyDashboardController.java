package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractDashboardController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlik;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.28.0
 * @since 11.11.2026 09:19
 */
@Named
@ViewScoped
@Slf4j
public class AyDashboardController extends AbstractDashboardController {
    @Serial
    private static final long serialVersionUID = -2538788359764856786L;

    @Inject
    private AyDashboardService ayDashboardService;

    @Getter
    @Setter
    private long toplamUye;
    @Getter
    @Setter
    private long aktifUye;
    @Getter
    @Setter
    private long bugunYeniKayit;
    @Getter
    @Setter
    private long buAyYeniKayit;
    @Getter
    @Setter
    private long toplamEtkinlik;
    @Getter
    @Setter
    private long yaklasanEtkinlik;
    @Getter
    @Setter
    private long buAyEtkinlik;
    @Getter
    @Setter
    private long etkinlikKatilim;
    @Getter
    @Setter
    private long toplamGirisCikis;
    @Getter
    @Setter
    private long bugunGirisCikis;
    @Getter
    @Setter
    private long buAyGirisCikis;
    @Getter
    @Setter
    private long toplamKursKaydi;
    @Getter
    @Setter
    private long toplamAktiviteKaydi;
    @Getter
    @Setter
    private long toplamBirim;

    @Getter
    @Setter
    private List<AyKisi> sonUyeler = new ArrayList<>();
    @Getter
    @Setter
    private List<AyEtkinlik> sonEtkinlikler = new ArrayList<>();
    @Getter
    @Setter
    private List<AyGirisCikis> sonGirisCikislar = new ArrayList<>();

    @Getter
    @Setter
    private String aylikUyeTrendJson = "";
    @Getter
    @Setter
    private String aylikEtkinlikTrendJson = "";
    @Getter
    @Setter
    private String birimDagilimJson = "";
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
            ayDashboardService.checkDashboardPermission();

            toplamUye = ayDashboardService.toplamUyeSayisi();
            aktifUye = ayDashboardService.aktifUyeSayisi();
            bugunYeniKayit = ayDashboardService.bugunYeniKayitSayisi();
            buAyYeniKayit = ayDashboardService.buAyYeniKayitSayisi();
            toplamEtkinlik = ayDashboardService.toplamEtkinlikSayisi();
            yaklasanEtkinlik = ayDashboardService.yaklasanEtkinlikSayisi();
            buAyEtkinlik = ayDashboardService.buAyEtkinlikSayisi();
            etkinlikKatilim = ayDashboardService.etkinlikKatilimSayisi();
            toplamGirisCikis = ayDashboardService.toplamGirisCikisSayisi();
            bugunGirisCikis = ayDashboardService.bugunGirisCikisSayisi();
            buAyGirisCikis = ayDashboardService.buAyGirisCikisSayisi();
            toplamKursKaydi = ayDashboardService.toplamKursKaydiSayisi();
            toplamAktiviteKaydi = ayDashboardService.toplamAktiviteKaydiSayisi();
            toplamBirim = ayDashboardService.toplamBirimSayisi();

            sonUyeler = ayDashboardService.sonUyeler();
            sonEtkinlikler = ayDashboardService.sonEtkinlikler();
            sonGirisCikislar = ayDashboardService.sonGirisCikislar();

            aylikUyeTrendJson = createMonthlyTrend(ayDashboardService.aylikUyeTrendi(), "Yeni Üye");
            aylikEtkinlikTrendJson = createMonthlyTrend(ayDashboardService.aylikEtkinlikTrendi(), "Etkinlik");
            birimDagilimJson = createDistribution(ayDashboardService.birimDagilimi(), "Birim", "doughnut");
            cinsiyetDagilimJson = createDistribution(ayDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = createDistribution(ayDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = createDistribution(ayDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Aktif Yaşam dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Aktif Yaşam dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }
}