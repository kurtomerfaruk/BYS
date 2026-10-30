package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkBireyselGorusme;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHastaTedaviSekli;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkTalep;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.service.PkDashboardService;

import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 30.10.2026 09:53
 */
@Named
@ViewScoped
@Slf4j
public class PkDashboardController implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 7853435034386668359L;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final DateTimeFormatter ETIKET_FORMAT = DateTimeFormatter.ofPattern("MM.yyyy");
    private static final List<String> GRAFIK_RENKLERI = List.of(
            "rgba(58,122,224,0.85)", "rgba(89,194,148,0.85)", "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)", "rgba(149,97,226,0.85)", "rgba(69,170,242,0.85)"
    );

    @Inject
    private PkDashboardService pkDashboardService;

    @Getter
    @Setter
    private long toplamDanisan;
    @Getter @Setter private long aktifDanisan;
    @Getter @Setter private long bugunYeniBasvuru;
    @Getter @Setter private long buAyYeniBasvuru;
    @Getter @Setter private long bugunGorusme;
    @Getter @Setter private long buAyGorusme;
    @Getter @Setter private long bekleyenTalep;
    @Getter @Setter private long buAyTaburcu;
    @Getter @Setter private long yatiliDanisan;
    @Getter @Setter private long ayaktanDanisan;
    @Getter @Setter private long verilenHizmet;
    @Getter @Setter private long hemsireGozlemFormu;
    @Getter @Setter private long revirAnemnez;
    @Getter @Setter private long naranon;
    @Getter @Setter private long aktifPersonel;
    @Getter @Setter private long izinliPersonel;
    @Getter @Setter private long kontrolZamaniYaklasanDanisan;

    @Getter @Setter private List<PkHasta> sonDanisanlar = new ArrayList<>();
    @Getter @Setter private List<PkBireyselGorusme> sonGorusmeler = new ArrayList<>();
    @Getter @Setter private List<PkTalep> bekleyenTalepler = new ArrayList<>();
    @Getter @Setter private List<PkHastaTedaviSekli> sonTaburcuIslemleri = new ArrayList<>();

    @Getter @Setter private String aylikBasvuruTrendJson = "";
    @Getter @Setter private String hizmetTuruDagilimJson = "";
    @Getter @Setter private String tedaviSekliDagilimJson = "";
    @Getter @Setter private String cinsiyetDagilimJson = "";
    @Getter @Setter private String yasDagilimiJson = "";
    @Getter @Setter private String ilceDagilimJson = "";

    @Getter @Setter private boolean yetkili = true;
    @Getter @Setter private String hataMesaji;

    @PostConstruct
    public void init() {
        try {
            pkDashboardService.checkDashboardPermission();

            toplamDanisan = pkDashboardService.toplamDanisanSayisi();
            aktifDanisan = pkDashboardService.aktifDanisanSayisi();
            bugunYeniBasvuru = pkDashboardService.bugunYeniBasvuruSayisi();
            buAyYeniBasvuru = pkDashboardService.buAyYeniBasvuruSayisi();
            bugunGorusme = pkDashboardService.bugunGorusmeSayisi();
            buAyGorusme = pkDashboardService.buAyGorusmeSayisi();
            bekleyenTalep = pkDashboardService.bekleyenTalepSayisi();
            buAyTaburcu = pkDashboardService.buAyTaburcuSayisi();
            yatiliDanisan = pkDashboardService.yatiliDanisanSayisi();
            ayaktanDanisan = pkDashboardService.ayaktanDanisanSayisi();
            verilenHizmet = pkDashboardService.verilenHizmetSayisi();
            hemsireGozlemFormu = pkDashboardService.hemsireGozlemFormuSayisi();
            revirAnemnez = pkDashboardService.revirAnemnezSayisi();
            naranon = pkDashboardService.naranonSayisi();
            aktifPersonel = pkDashboardService.aktifPersonelSayisi();
            izinliPersonel = pkDashboardService.izinliPersonelSayisi();
            kontrolZamaniYaklasanDanisan = pkDashboardService.kontrolZamaniYaklasanDanisanSayisi();

            sonDanisanlar = pkDashboardService.sonEklenenDanisanlar();
            sonGorusmeler = pkDashboardService.sonGorusmeler();
            bekleyenTalepler = pkDashboardService.bekleyenTalepler();
            sonTaburcuIslemleri = pkDashboardService.sonTaburcuIslemleri();

            aylikBasvuruTrendJson = aylikBasvuruTrendOlustur(pkDashboardService.aylikBasvuruTrendi());
            hizmetTuruDagilimJson = hizmetTuruDagilimOlustur(pkDashboardService.hizmetTuruDagilimi());
            tedaviSekliDagilimJson = tedaviSekliDagilimOlustur(pkDashboardService.tedaviSekliDagilimi());
            cinsiyetDagilimJson = dagilimOlustur(pkDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = dagilimOlustur(pkDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = dagilimOlustur(pkDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Psikiyatri dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Psikiyatri dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }

    public void yenile() {
        init();
    }

    private String aylikBasvuruTrendOlustur(List<Object[]> veri) {
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        for (Object[] row : veri) {
            Integer yil = ((Number) row[0]).intValue();
            Integer ay = ((Number) row[1]).intValue();
            Long adet = ((Number) row[2]).longValue();
            etiketler.add(String.format("%02d.%d", ay, yil));
            sayilar.add(adet);
        }
        if (etiketler.isEmpty()) {
            return "";
        }

        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", "Yeni Başvuru");
        dataset.put("data", sayilar);
        dataset.put("borderColor", "rgba(58,122,224,1)");
        dataset.put("backgroundColor", "rgba(58,122,224,0.6)");
        dataset.put("borderWidth", 1);

        return grafikConfigOlustur("bar", etiketler, List.of(dataset), temelSecenekler(false));
    }

    private String hizmetTuruDagilimOlustur(List<Object[]> veri) {
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int index = 0;
        for (Object[] row : veri) {
            String tanim = (String) row[0];
            Long adet = ((Number) row[1]).longValue();
            if (tanim != null && adet != null) {
                etiketler.add(tanim);
                sayilar.add(adet);
                renkler.add(GRAFIK_RENKLERI.get(index % GRAFIK_RENKLERI.size()));
                index++;
            }
        }
        if (etiketler.isEmpty()) {
            return "";
        }

        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", "Verilen Hizmet");
        dataset.put("data", sayilar);
        dataset.put("backgroundColor", renkler);

        return grafikConfigOlustur("doughnut", etiketler, List.of(dataset), temelSecenekler(true));
    }

    private String tedaviSekliDagilimOlustur(List<Object[]> veri) {
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int index = 0;
        for (Object[] row : veri) {
            Object sekli = row[0];
            Long adet = ((Number) row[1]).longValue();
            if (sekli != null && adet != null) {
                etiketler.add(sekli.toString());
                sayilar.add(adet);
                renkler.add(GRAFIK_RENKLERI.get(index % GRAFIK_RENKLERI.size()));
                index++;
            }
        }
        if (etiketler.isEmpty()) {
            return "";
        }

        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", "Tedavi Şekli");
        dataset.put("data", sayilar);
        dataset.put("backgroundColor", renkler);

        return grafikConfigOlustur("pie", etiketler, List.of(dataset), temelSecenekler(true));
    }

    private Map<String, Object> temelSecenekler(Boolean legend) {
        Map<String, Object> options = new LinkedHashMap<>();
        if (legend != null) {
            Map<String, Object> plugins = new LinkedHashMap<>();
            Map<String, Object> legendMap = new LinkedHashMap<>();
            legendMap.put("display", legend);
            plugins.put("legend", legendMap);
            options.put("plugins", plugins);
        }
        return options;
    }

    private String dagilimOlustur(List<Object[]> veri, String label, String tip) {
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int index = 0;
        for (Object[] row : veri) {
            Object key = row[0];
            Long adet = ((Number) row[1]).longValue();
            if (key != null && adet != null) {
                etiketler.add(key.toString());
                sayilar.add(adet);
                renkler.add(GRAFIK_RENKLERI.get(index % GRAFIK_RENKLERI.size()));
                index++;
            }
        }
        if (etiketler.isEmpty()) {
            return "";
        }
        Map<String, Object> dataset = new LinkedHashMap<>();
        dataset.put("label", label);
        dataset.put("data", sayilar);
        dataset.put("backgroundColor", renkler);
        return grafikConfigOlustur(tip, etiketler, List.of(dataset), new LinkedHashMap<>());
    }

    private String grafikConfigOlustur(String tip, List<String> etiketler,
                                       List<Map<String, Object>> datasetler, Map<String, Object> secenekler) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("labels", etiketler);
        data.put("datasets", datasetler);

        Map<String, Object> config = new LinkedHashMap<>();
        config.put("type", tip);
        config.put("data", data);
        config.put("options", secenekler);

        try {
            return JSON_MAPPER.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            log.error("Psikiyatri dashboard grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }
}

