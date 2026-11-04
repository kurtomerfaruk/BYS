package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbTalep;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbVerilecekHizmet;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service.EdbDashboardService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 4.11.2026 10:15
 */
@Named
@ViewScoped
@Slf4j
public class EdbDashboardController implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final List<String> GRAFIK_RENKLERI = List.of(
            "rgba(58,122,224,0.85)", "rgba(89,194,148,0.85)", "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)", "rgba(149,97,226,0.85)", "rgba(69,170,242,0.85)"
    );

    @Inject
    private EdbDashboardService edbDashboardService;

    @Getter
    @Setter
    private long toplamBasvuru;
    @Getter @Setter private long bugunBasvuru;
    @Getter @Setter private long buAyBasvuru;
    @Getter @Setter private long toplamTalep;
    @Getter @Setter private long bekleyenTalep;
    @Getter @Setter private long buAyTalep;
    @Getter @Setter private long toplamVerilecekHizmet;
    @Getter @Setter private long bekleyenVerilecekHizmet;
    @Getter @Setter private long tamamlananVerilecekHizmet;
    @Getter @Setter private long buAyVerilecekHizmet;
    @Getter @Setter private long toplamMemnuniyetAnketi;
    @Getter @Setter private long toplamEkip;
    @Getter @Setter private long toplamPersonel;

    @Getter @Setter private List<EdbBasvuru> sonBasvurular = new ArrayList<>();
    @Getter @Setter private List<EdbTalep> sonTalepler = new ArrayList<>();
    @Getter @Setter private List<EdbVerilecekHizmet> sonVerilecekHizmetler = new ArrayList<>();
    @Getter @Setter private List<EdbTalep> bekleyenTalepler = new ArrayList<>();

    @Getter @Setter private String aylikBasvuruTrendJson = "";
    @Getter @Setter private String hizmetTuruDagilimJson = "";
    @Getter @Setter private String talepDurumuDagilimJson = "";
    @Getter @Setter private String cinsiyetDagilimJson = "";
    @Getter @Setter private String yasDagilimiJson = "";
    @Getter @Setter private String ilceDagilimJson = "";

    @Getter @Setter private boolean yetkili = true;
    @Getter @Setter private String hataMesaji;

    @PostConstruct
    public void init() {
        try {
            edbDashboardService.checkDashboardPermission();

            toplamBasvuru = edbDashboardService.toplamBasvuruSayisi();
            bugunBasvuru = edbDashboardService.bugunBasvuruSayisi();
            buAyBasvuru = edbDashboardService.buAyBasvuruSayisi();
            toplamTalep = edbDashboardService.toplamTalepSayisi();
            bekleyenTalep = edbDashboardService.bekleyenTalepSayisi();
            buAyTalep = edbDashboardService.buAyTalepSayisi();
            toplamVerilecekHizmet = edbDashboardService.toplamVerilecekHizmetSayisi();
            bekleyenVerilecekHizmet = edbDashboardService.bekleyenVerilecekHizmetSayisi();
            tamamlananVerilecekHizmet = edbDashboardService.tamamlananVerilecekHizmetSayisi();
            buAyVerilecekHizmet = edbDashboardService.buAyVerilecekHizmetSayisi();
            toplamMemnuniyetAnketi = edbDashboardService.toplamMemnuniyetAnketiSayisi();
            toplamEkip = edbDashboardService.toplamEkipSayisi();
            toplamPersonel = edbDashboardService.toplamPersonelSayisi();

            sonBasvurular = edbDashboardService.sonBasvurular();
            sonTalepler = edbDashboardService.sonTalepler();
            sonVerilecekHizmetler = edbDashboardService.sonVerilecekHizmetler();
            bekleyenTalepler = edbDashboardService.bekleyenTalepler();

            aylikBasvuruTrendJson = aylikTrendOlustur(edbDashboardService.aylikBasvuruTrendi());
            talepDurumuDagilimJson = dagilimOlustur(edbDashboardService.talepDurumuDagilimi(), "Talep Durumu", "pie");
            cinsiyetDagilimJson = dagilimOlustur(edbDashboardService.cinsiyetDagilimi(), "Cinsiyet", "pie");
            yasDagilimiJson = dagilimOlustur(edbDashboardService.yasDagilimi(), "Yaş Grubu", "bar");
            ilceDagilimJson = dagilimOlustur(edbDashboardService.ilceDagilimi(), "İlçe", "bar");

        } catch (SecurityException e) {
            yetkili = false;
            hataMesaji = e.getMessage();
            log.warn("Evim Dünyalara Bedel dashboard yetki hatası: {}", e.getMessage());
        } catch (Exception e) {
            yetkili = false;
            hataMesaji = "Dashboard verileri yüklenirken hata oluştu.";
            log.error("Evim Dünyalara Bedel dashboard yüklenemedi", e);
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
                if(key instanceof Enum<?>){
                    String enumValue = ((BaseEnum)key).getDisplayValue();
                    etiketler.add(enumValue);
                }else{
                    etiketler.add(key.toString());
                }

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
            log.error("Evim Dünyalara Bedel dashboard grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }
}
