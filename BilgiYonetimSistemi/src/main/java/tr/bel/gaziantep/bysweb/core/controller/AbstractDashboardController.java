package tr.bel.gaziantep.bysweb.core.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;

import java.io.Serial;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.27.0
 * @since 5.11.2026 08:38
 */
@Slf4j
public abstract class AbstractDashboardController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1699666691703526864L;
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final List<String> GRAPHIC_COLORS = List.of(
            "rgba(58,122,224,0.85)", "rgba(89,194,148,0.85)", "rgba(246,153,63,0.85)",
            "rgba(228,77,99,0.85)", "rgba(149,97,226,0.85)", "rgba(69,170,242,0.85)"
    );

    protected abstract void init();

    public void yenile() {
        init();
    }

    protected String createDistribution(List<Object[]> veri, String label, String tip, EnumModul modul) {
        List<String> etiketler = new ArrayList<>();
        List<Number> sayilar = new ArrayList<>();
        List<String> renkler = new ArrayList<>();
        int index = 0;
        for (Object[] row : veri) {
            Object key = row[0];
            Long adet = ((Number) row[1]).longValue();
            if (key != null && adet != null) {
                if (key instanceof Enum<?>) {
                    String enumValue = ((BaseEnum) key).getDisplayValue();
                    etiketler.add(enumValue);
                } else {
                    etiketler.add(key.toString());
                }
                sayilar.add(adet);
                renkler.add(GRAPHIC_COLORS.get(index % GRAPHIC_COLORS.size()));
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
        return createChartConfig(tip, etiketler, List.of(dataset), new LinkedHashMap<>(), modul);
    }

    protected String createMonthlyTrend(List<Object[]> veri, String label, EnumModul modul) {
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
        dataset.put("label", label);
        dataset.put("data", sayilar);
        dataset.put("borderColor", "rgba(58,122,224,1)");
        dataset.put("backgroundColor", "rgba(58,122,224,0.6)");
        dataset.put("borderWidth", 1);
        return createChartConfig("bar", etiketler, List.of(dataset), new LinkedHashMap<>(), modul);
    }

    private String createChartConfig(String tip, List<String> etiketler,
                                     List<Map<String, Object>> datasetler, Map<String, Object> secenekler, EnumModul modul) {
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
            log.error(modul.getDisplayValue() + " dashboard grafik JSON'u üretilirken hata oluştu.", e);
            return "";
        }
    }
}
