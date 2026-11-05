package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractDashboardController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuruMalzemeTeslimi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtFizikTedavi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 5.11.2026 08:46
 */
@Named
@ViewScoped
@Slf4j
public class OrtDashboardController extends AbstractDashboardController {
    @Serial
    private static final long serialVersionUID = -9012530024029829861L;

    @Inject
    private OrtDashboardService ortDashboardService;

    @Getter
    @Setter
    private long toplamHasta;
    @Getter
    @Setter
    private long toplamBasvuru;
    @Getter
    @Setter
    private long bekleyenBasvuru;
    @Getter
    @Setter
    private long bugunBasvuru;
    @Getter
    @Setter
    private long buAyBasvuru;
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
    private long toplamFizikTedavi;
    @Getter
    @Setter
    private long bekleyenFizikTedavi;
    @Getter
    @Setter
    private long tamamlananFizikTedavi;
    @Getter
    @Setter
    private long toplamMalzemeTalep;
    @Getter
    @Setter
    private long teslimEdilmemisMalzemeTalep;
    @Getter
    @Setter
    private long toplamStok;
    @Getter
    @Setter
    private long toplamProje;

    @Getter
    @Setter
    private List<OrtBasvuru> sonBasvurular = new ArrayList<>();
    @Getter
    @Setter
    private List<OrtBasvuru> bekleyenBasvurular = new ArrayList<>();
    @Getter
    @Setter
    private List<OrtRandevu> sonRandevular = new ArrayList<>();
    @Getter
    @Setter
    private List<OrtFizikTedavi> sonFizikTedaviler = new ArrayList<>();
    @Getter
    @Setter
    private List<OrtBasvuruMalzemeTeslimi> sonMalzemeTeslimleri = new ArrayList<>();

    @Getter
    @Setter
    private String aylikBasvuruTrendJson = "";
    @Getter
    @Setter
    private String basvuruDurumuDagilimJson = "";

    @Getter
    @Setter
    private String fizikTedaviDurumuDagilimJson = "";
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
    @Override
    public void init() {
        try {
            ortDashboardService.checkDashboardPermission();

            toplamHasta = ortDashboardService.toplamHastaSayisi();
            toplamBasvuru = ortDashboardService.toplamBasvuruSayisi();
            bekleyenBasvuru = ortDashboardService.bekleyenBasvuruSayisi();
            bugunBasvuru = ortDashboardService.bugunBasvuruSayisi();
            buAyBasvuru = ortDashboardService.buAyBasvuruSayisi();
            toplamRandevu = ortDashboardService.toplamRandevuSayisi();
            bugunRandevu = ortDashboardService.bugunRandevuSayisi();
            buAyRandevu = ortDashboardService.buAyRandevuSayisi();
            toplamFizikTedavi = ortDashboardService.toplamFizikTedaviSayisi();
            bekleyenFizikTedavi = ortDashboardService.bekleyenFizikTedaviSayisi();
            tamamlananFizikTedavi = ortDashboardService.tamamlananFizikTedaviSayisi();
            toplamMalzemeTalep = ortDashboardService.toplamMalzemeTalepSayisi();
            teslimEdilmemisMalzemeTalep = ortDashboardService.teslimEdilmemisMalzemeTalepSayisi();
            toplamStok = ortDashboardService.toplamStokSayisi();
            toplamProje = ortDashboardService.toplamProjeSayisi();

            sonBasvurular = ortDashboardService.sonBasvurular();
            bekleyenBasvurular = ortDashboardService.bekleyenBasvurular();
            sonRandevular = ortDashboardService.sonRandevular();
            sonFizikTedaviler = ortDashboardService.sonFizikTedaviler();
            sonMalzemeTeslimleri = ortDashboardService.sonMalzemeTeslimleri();

            aylikBasvuruTrendJson = createMonthlyTrend(ortDashboardService.aylikBasvuruTrendi(), "Başvuru", EnumModul.ORTEZ_PROTEZ);
            basvuruDurumuDagilimJson = createDistribution(ortDashboardService.basvuruDurumuDagilimi(), "Başvuru Durumu", "pie", EnumModul.ORTEZ_PROTEZ);
            fizikTedaviDurumuDagilimJson = createDistribution(ortDashboardService.fizikTedaviDurumuDagilimi(), "Fizik Tedavi Durumu", "doughnut", EnumModul.ORTEZ_PROTEZ);
            cinsiyetDagilimJson = createDistribution(ortDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie", EnumModul.ORTEZ_PROTEZ);
            yasDagilimiJson = createDistribution(ortDashboardService.yasDagilimi(), "Yaş Grubu", "bar", EnumModul.ORTEZ_PROTEZ);
            ilceDagilimJson = createDistribution(ortDashboardService.ilceDagilimi(), "İlçe", "bar", EnumModul.ORTEZ_PROTEZ);

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Ortez-Protez dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Ortez-Protez dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }


}
