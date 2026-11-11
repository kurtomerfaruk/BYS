package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractDashboardController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlik;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeTalep;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.28.0
 * @since 11.11.2026 10:49
 */
@Named
@ViewScoped
@Slf4j
public class MeDashboardController extends AbstractDashboardController {

    @Serial
    private static final long serialVersionUID = -7233122276402891174L;

    @Inject
    private MeDashboardService meDashboardService;

    @Getter
    @Setter
    private long toplamKisi;
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
    private long toplamTalep;
    @Getter
    @Setter
    private long bekleyenTalep;
    @Getter
    @Setter
    private long buAyTalep;
    @Getter
    @Setter
    private long toplamAnket;
    @Getter
    @Setter
    private long toplamPersonel;

    @Getter
    @Setter
    private List<MeKisi> sonKisiler = new ArrayList<>();
    @Getter
    @Setter
    private List<MeEtkinlik> sonEtkinlikler = new ArrayList<>();
    @Getter
    @Setter
    private List<MeGirisCikis> sonGirisCikislar = new ArrayList<>();
    @Getter
    @Setter
    private List<MeTalep> sonTalepler = new ArrayList<>();
    @Getter
    @Setter
    private List<MeTalep> bekleyenTalepler = new ArrayList<>();

    @Getter
    @Setter
    private String aylikKisiTrendJson = "";
    @Getter
    @Setter
    private String aylikEtkinlikTrendJson = "";
    @Getter
    @Setter
    private String talepDurumuDagilimJson = "";
    @Getter
    @Setter
    private String anketMemnuniyetDagilimJson = "";
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
            meDashboardService.checkDashboardPermission();

            toplamKisi = meDashboardService.toplamKisiSayisi();
            bugunYeniKayit = meDashboardService.bugunYeniKayitSayisi();
            buAyYeniKayit = meDashboardService.buAyYeniKayitSayisi();
            toplamEtkinlik = meDashboardService.toplamEtkinlikSayisi();
            yaklasanEtkinlik = meDashboardService.yaklasanEtkinlikSayisi();
            buAyEtkinlik = meDashboardService.buAyEtkinlikSayisi();
            etkinlikKatilim = meDashboardService.etkinlikKatilimSayisi();
            toplamGirisCikis = meDashboardService.toplamGirisCikisSayisi();
            bugunGirisCikis = meDashboardService.bugunGirisCikisSayisi();
            buAyGirisCikis = meDashboardService.buAyGirisCikisSayisi();
            toplamTalep = meDashboardService.toplamTalepSayisi();
            bekleyenTalep = meDashboardService.bekleyenTalepSayisi();
            buAyTalep = meDashboardService.buAyTalepSayisi();
            toplamAnket = meDashboardService.toplamAnketSayisi();
            toplamPersonel = meDashboardService.toplamPersonelSayisi();

            sonKisiler = meDashboardService.sonKisiler();
            sonEtkinlikler = meDashboardService.sonEtkinlikler();
            sonGirisCikislar = meDashboardService.sonGirisCikislar();
            sonTalepler = meDashboardService.sonTalepler();
            bekleyenTalepler = meDashboardService.bekleyenTalepler();

            aylikKisiTrendJson = createMonthlyTrend(meDashboardService.aylikKisiTrendi(), "Yeni Kayıt");
            aylikEtkinlikTrendJson = createMonthlyTrend(meDashboardService.aylikEtkinlikTrendi(), "Etkinlik");
            talepDurumuDagilimJson = createDistribution(meDashboardService.talepDurumuDagilimi(), "Talep Durumu", "pie");
            anketMemnuniyetDagilimJson = createDistribution(meDashboardService.anketMemnuniyetDagilimi(), "Memnuniyet", "doughnut");
            cinsiyetDagilimJson = createDistribution(meDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = createDistribution(meDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = createDistribution(meDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Moral Evi dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Moral Evi dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }

}