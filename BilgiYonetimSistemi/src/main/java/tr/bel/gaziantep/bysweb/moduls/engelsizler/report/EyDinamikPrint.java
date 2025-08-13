package tr.bel.gaziantep.bysweb.moduls.engelsizler.report;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.entity.DynamicReportModel;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumOperator;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 08:48
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class EyDinamikPrint extends AbstractReportController {
    @Serial
    private static final long serialVersionUID = 1101268376306256267L;
    private static final String PATH = "engelsizler";

    private EnumRaporTuru raporTuru;
    private Map<String, String> columns;
    private DynamicReportModel dynamicReportModel;
    private List<DynamicReportModel> dynamicReportModels;

    private static final Map<EnumOperator, String> SQL_OPERATORS = Map.of(
            EnumOperator.ESITTIR, "%s='%s'",
            EnumOperator.KUCUKTUR, "%s<'%s'",
            EnumOperator.KUCUK_ESITTIR, "%s<='%s'",
            EnumOperator.BUYUKTUR, "%s>'%s'",
            EnumOperator.BUYUK_ESITTIR, "%s>='%s'",
            EnumOperator.ILE_BASLAYAN, "%s LIKE '%s%%'",
            EnumOperator.ILE_BITEN, "%s LIKE '%%%s'",
            EnumOperator.ICEREN, "%s LIKE '%%%s%%'"
    );

    @PostConstruct
    public void init() {
        dynamicReportModels = new ArrayList<>();
        dynamicReportModel = DynamicReportModel.builder().operator(EnumOperator.ESITTIR).build();
        generateColumn();
    }

    private void generateColumn() {
        columns = new LinkedHashMap<>();
        columns.put("G.CINSIYET", "Cinsiyet");
        columns.put("EK.ANKET_DURUMU", "Anket Durumu");
        columns.put("G.ILCE_ID", "İlçe");
        columns.put("G.MAHALLE_ID", "Mahalle");
        columns.put("G.MEDENI_DURUM", "Medeni Durum");
        columns.put("G.DURUM", "Durum");
        columns.put("G.EGITIM_DURUMU", "Eğitim");
        columns.put("G.SABIKA_KAYDI", "Sabıka Kaydı");
        columns.put("G.ISKUR_KAYDI", "İşKur Kaydı");
        columns.put("G.EHLIYET", "Ehliyet");
        columns.put("G.SOSYAL_GUVENCE", "Aile Sosyal Güvence");
        columns.put("EK.TOPLAM_VUCUT_KAYIP_ORANI", "Toplam Vücut Kayıp Oranı");
    }

    public void addToModel() {
        DynamicReportModel reportModel = dynamicReportModels.stream()
                .filter(report -> report.getColumnKey().equals(dynamicReportModel.getColumnKey())
                        && report.getValue().equals(dynamicReportModel.getValue())
                        && report.getOperator().equals(dynamicReportModel.getOperator())
                        && report.getColumnValue().equals(dynamicReportModel.getColumnValue()))
                .findFirst()
                .orElse(null);
        if (reportModel == null) {
            String value = dynamicReportModel.getValue();
            if (value.equals("true") || value.equals("false")) {
                value = value.equals("true") ? "1" : "0";
            }
            reportModel = DynamicReportModel.builder()
                    .columnKey(dynamicReportModel.getColumnKey())
                    .columnValue(columns.get(dynamicReportModel.getColumnKey()))
                    .operator(dynamicReportModel.getOperator())
                    .value(value)
                    .build();
            dynamicReportModels.add(reportModel);
            dynamicReportModel = DynamicReportModel.builder().operator(EnumOperator.ESITTIR).build();
        }
    }

    public void removeModel(DynamicReportModel dynamicReportModel) {
        try {
            if (dynamicReportModels.contains(dynamicReportModel)) {
                dynamicReportModels.remove(dynamicReportModel);
                FacesUtil.successMessage("satirSilindi");
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public String print() {
        try {
            setCompileFileName(PATH + File.separator + "eydinamik_rapor");
            Map<String, Object> parameterMap = new HashMap<>();

            String whereClause = generateWhereClause();
            // whereType A = All C = Custom
            if (StringUtil.isBlank(whereClause)) {
                parameterMap.put("whereType", "A");
            } else {
                parameterMap.put("whereType", "C");
            }
            parameterMap.put("whereClause", whereClause);

            setExportOption(raporTuru);

            if (raporTuru == EnumRaporTuru.PDF) {

                ServletContext sc = Util.getServletContext();
                generateAttribute(sc, parameterMap);
                sc.getRequestDispatcher("/ReportServlet");
                PrimeFaces.current().executeScript("PF('PrintDialog').show()");
            } else {
                super.prepareReport(parameterMap);
            }

        } catch (JRException | IOException e) {
            log.error(null,e);
        }
        return null;
    }



    private String generateWhereClause() {
        StringBuilder builder = new StringBuilder(" EK.AKTIF=1 ");
        for (DynamicReportModel reportModel : dynamicReportModels) {
            String value = convertBooleanValue(reportModel.getValue());
            builder.append(" AND ");
            if (reportModel.getOperator() == EnumOperator.ARASINDA) {
                builder.append(String.format("(%s BETWEEN '%s' AND '%s')",
                        reportModel.getColumnKey(),
                        value,
                        reportModel.getValueTo()));
            } else {
                String sqlFormat = SQL_OPERATORS.get(reportModel.getOperator());
                if (sqlFormat != null) {
                    builder.append(String.format(sqlFormat, reportModel.getColumnKey(), value));
                }
            }
        }
        return builder.toString();
    }

    private String convertBooleanValue(String value) {
        if ("true".equalsIgnoreCase(value)) {
            return "1";
        } else if ("false".equalsIgnoreCase(value)) {
            return "0";
        }
        return value;
    }

//    private String generateWhereClause() {
//        StringBuilder builder = new StringBuilder();
//        builder.append(" EK.AKTIF=1 ");
//        for (DynamicReportModel reportModel : dynamicReportModels) {
//            String value = reportModel.getValue();
//            if (value.equals("true") || value.equals("false")) {
//                value = value.equals("true") ? "1" : "0";
//            }
//            builder.append(" AND ");
//            if (reportModel.getOperator().equals(EnumOperator.ESITTIR)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("='")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.KUCUKTUR)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("<'")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.KUCUK_ESITTIR)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("<='")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.BUYUKTUR)) {
//                builder.append(reportModel.getColumnKey())
//                        .append(">'")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.BUYUK_ESITTIR)) {
//                builder.append(reportModel.getColumnKey())
//                        .append(">='")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.ILE_BASLAYAN)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("LIKE '")
//                        .append(value)
//                        .append("%'");
//            } else if (reportModel.getOperator().equals(EnumOperator.ILE_BITEN)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("LIKE '%")
//                        .append(value)
//                        .append("'");
//            } else if (reportModel.getOperator().equals(EnumOperator.ICEREN)) {
//                builder.append(reportModel.getColumnKey())
//                        .append("LIKE '%")
//                        .append(value)
//                        .append("%'");
//            } else if (reportModel.getOperator().equals(EnumOperator.ARASINDA)) {
//                builder.append("(")
//                        .append(reportModel.getColumnKey())
//                        .append("BETWEEN '")
//                        .append(value)
//                        .append("' AND ")
//                        .append("'")
//                        .append(reportModel.getValueTo())
//                        .append("'")
//                        .append(")");
//            }
//        }
//        return builder.toString();
//    }
}
