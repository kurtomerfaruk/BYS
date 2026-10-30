package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

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
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracTamir;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.25.0
 * @since 30.10.2026 10:21
 */
@Named
@ViewScoped
@Slf4j
public class EyDashboardController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 4490819164960959952L;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final List<String> GRAFIK_RENKLERI = List.of(
            "rgba(58,122,224,0.85)", "rgba(89,194,148,0.85)", "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)", "rgba(149,97,226,0.85)", "rgba(69,170,242,0.85)"
    );

    @Inject
    private EyDashboardService eyDashboardService;

    @Getter
    @Setter
    private long toplamKisi;
    @Getter @Setter private long bugunYeniKayit;
    @Getter @Setter private long buAyYeniKayit;
    @Getter @Setter private long toplamTalep;
    @Getter @Setter private long bekleyenTalep;
    @Getter @Setter private long buAyTalep;
    @Getter @Setter private long toplamTamir;
    @Getter @Setter private long buAyTamir;
    @Getter @Setter private long toplamCihazTeslim;
    @Getter @Setter private long buAyCihazTeslim;
    @Getter @Setter private long sosyalIncelemeRaporu;
    @Getter @Setter private long buAySosyalInceleme;
    @Getter @Setter private long kullanilanCihaz;
    @Getter @Setter private long engelGrubuSayisi;

    @Getter @Setter private List<EyKisi> sonKisiler = new ArrayList<>();
    @Getter @Setter private List<EyTalep> sonTalepler = new ArrayList<>();
    @Getter @Setter private List<EyTalep> bekleyenTalepler = new ArrayList<>();
    @Getter @Setter private List<EyAracTamir> sonTamirler = new ArrayList<>();
    @Getter @Setter private List<EyAracCihazTeslimi> sonCihazTeslimleri = new ArrayList<>();

    @Getter @Setter private String aylikTalepTrendJson = "";
    @Getter @Setter private String engelGrubuDagilimJson = "";
    @Getter @Setter private String talepDurumuDagilimJson = "";
    @Getter @Setter private String cinsiyetDagilimJson = "";
    @Getter @Setter private String yasDagilimiJson = "";
    @Getter @Setter private String ilceDagilimJson = "";

    @Getter @Setter private boolean yetkili = true;
    @Getter @Setter private String hataMesaji;

    @PostConstruct
    public void init() {
        try {
            eyDashboardService.checkDashboardPermission();

            toplamKisi = eyDashboardService.toplamKisiSayisi();
            bugunYeniKayit = eyDashboardService.bugunYeniKayitSayisi();
            buAyYeniKayit = eyDashboardService.buAyYeniKayitSayisi();
            toplamTalep = eyDashboardService.toplamTalepSayisi();
            bekleyenTalep = eyDashboardService.bekleyenTalepSayisi();
            buAyTalep = eyDashboardService.buAyTalepSayisi();
            toplamTamir = eyDashboardService.toplamTamirSayisi();
            buAyTamir = eyDashboardService.buAyTamirSayisi();
            toplamCihazTeslim = eyDashboardService.toplamCihazTeslimSayisi();
            buAyCihazTeslim = eyDashboardService.buAyCihazTeslimSayisi();
            sosyalIncelemeRaporu = eyDashboardService.sosyalIncelemeRaporuSayisi();
            buAySosyalInceleme = eyDashboardService.buAySosyalIncelemeSayisi();
            kullanilanCihaz = eyDashboardService.kullanilanCihazSayisi();
            engelGrubuSayisi = eyDashboardService.engelGrubuSayisi();

            sonKisiler = eyDashboardService.sonKisiler();
            sonTalepler = eyDashboardService.sonTalepler();
            bekleyenTalepler = eyDashboardService.bekleyenTalepler();
            sonTamirler = eyDashboardService.sonTamirler();
            sonCihazTeslimleri = eyDashboardService.sonCihazTeslimleri();

            aylikTalepTrendJson = aylikTalepTrendOlustur(eyDashboardService.aylikTalepTrendi());
            engelGrubuDagilimJson = dagilimOlustur(eyDashboardService.engelGrubuDagilimi(), "Engel Grubu", "doughnut");
            talepDurumuDagilimJson = dagilimOlustur(eyDashboardService.talepDurumuDagilimi(), "Talep Durumu", "pie");
            cinsiyetDagilimJson = dagilimOlustur(eyDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = dagilimOlustur(eyDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = dagilimOlustur(eyDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Engelsizler dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Engelsizler dashboard yüklenemedi", e);
            FacesUtil.addErrorMessage(hataMesaji);
        }
    }

    public void yenile() {
        init();
    }

    private String aylikTalepTrendOlustur(List<Object[]> veri) {
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
        dataset.put("label", "Talep");
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
            log.error("Engelsizler dashboard grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }
}
