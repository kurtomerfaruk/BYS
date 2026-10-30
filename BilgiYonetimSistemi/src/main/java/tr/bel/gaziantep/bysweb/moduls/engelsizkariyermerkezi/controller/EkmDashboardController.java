package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

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
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmIsBasvuru;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 30.10.2026 11:13
 */
@Named
@ViewScoped
@Slf4j
public class EkmDashboardController implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -6570263092600435843L;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final List<String> GRAFIK_RENKLERI = List.of(
            "rgba(58,122,224,0.85)", "rgba(89,194,148,0.85)", "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)", "rgba(149,97,226,0.85)", "rgba(69,170,242,0.85)"
    );

    @Inject
    private EkmDashboardService ekmDashboardService;

    @Getter
    @Setter
    private long toplamBasvuru;
    @Getter @Setter private long bugunBasvuru;
    @Getter @Setter private long buAyBasvuru;
    @Getter @Setter private long toplamKursiyer;
    @Getter @Setter private long aktifKursiyer;
    @Getter @Setter private long toplamPozisyon;
    @Getter @Setter private long toplamGirisCikis;
    @Getter @Setter private long buAyGirisCikis;
    @Getter @Setter private long toplamKursKaydi;

    @Getter @Setter private List<EkmIsBasvuru> sonBasvurular = new ArrayList<>();
    @Getter @Setter private List<EkmKursiyer> sonKursiyerler = new ArrayList<>();
    @Getter @Setter private List<EkmGirisCikis> sonGirisCikislar = new ArrayList<>();

    @Getter @Setter private String aylikBasvuruTrendJson = "";
    @Getter @Setter private String pozisyonDagilimJson = "";
    @Getter @Setter private String kursDagilimJson = "";
    @Getter @Setter private String cinsiyetDagilimJson = "";
    @Getter @Setter private String yasDagilimiJson = "";
    @Getter @Setter private String ilceDagilimJson = "";

    @Getter @Setter private boolean yetkili = true;
    @Getter @Setter private String hataMesaji;

    @PostConstruct
    public void init() {
        try {
            ekmDashboardService.checkDashboardPermission();

            toplamBasvuru = ekmDashboardService.toplamBasvuruSayisi();
            bugunBasvuru = ekmDashboardService.bugunBasvuruSayisi();
            buAyBasvuru = ekmDashboardService.buAyBasvuruSayisi();
            toplamKursiyer = ekmDashboardService.toplamKursiyerSayisi();
            aktifKursiyer = ekmDashboardService.aktifKursiyerSayisi();
            toplamPozisyon = ekmDashboardService.toplamPozisyonSayisi();
            toplamGirisCikis = ekmDashboardService.toplamGirisCikisSayisi();
            buAyGirisCikis = ekmDashboardService.buAyGirisCikisSayisi();
            toplamKursKaydi = ekmDashboardService.toplamKursKaydiSayisi();

            sonBasvurular = ekmDashboardService.sonBasvurular();
            sonKursiyerler = ekmDashboardService.sonKursiyerler();
            sonGirisCikislar = ekmDashboardService.sonGirisCikislar();

            aylikBasvuruTrendJson = aylikTrendOlustur(ekmDashboardService.aylikBasvuruTrendi());
            pozisyonDagilimJson = dagilimOlustur(ekmDashboardService.pozisyonDagilimi(), "Pozisyon", "doughnut");
            kursDagilimJson = dagilimOlustur(ekmDashboardService.kursDagilimi(), "Kurs", "pie");
            cinsiyetDagilimJson = dagilimOlustur(ekmDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = dagilimOlustur(ekmDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = dagilimOlustur(ekmDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Engelsiz Kariyer Merkezi dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Engelsiz Kariyer Merkezi dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }

    public void yenile() {
        init();
    }

    private String aylikTrendOlustur(List<Object[]> veri) {
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
        dataset.put("label", "Başvuru");
        dataset.put("data", sayilar);
        dataset.put("borderColor", "rgba(58,122,224,1)");
        dataset.put("backgroundColor", "rgba(58,122,224,0.6)");
        dataset.put("borderWidth", 1);
        return grafikConfigOlustur("bar", etiketler, List.of(dataset), new LinkedHashMap<>());
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
            log.error("Engelsiz Kariyer Merkezi dashboard grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }
}