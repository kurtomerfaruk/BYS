package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import tr.bel.gaziantep.bysweb.moduls.genel.report.GnlRaporIstek;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.12.2025 14:16
 */
@Stateless
public class JasperReportService {

    public byte[] generateReport(List<Map<String, Object>> data, GnlRaporIstek raporIstek) {
        try {
            JasperDesign jasperDesign = createDynamicJasperDesign(raporIstek);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> parameters = prepareReportParameters(raporIstek);
            JRDataSource dataSource = new JRMapCollectionDataSource((Collection<Map<String, ?>>) (Collection<?>) data);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return exportReport(jasperPrint, raporIstek.getCiktiTipi());

        } catch (JRException e) {
            throw new RuntimeException("JasperReport hatası: " + e.getMessage(), e);
        }
    }


    private JasperDesign createDynamicJasperDesign(GnlRaporIstek raporIstek) throws JRException {

        JasperDesign design = new JasperDesign();
        design.setName("dynamic_report");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(555);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        design.setQuery(new JRDesignQuery()); // boş query

        JRDesignBand header = new JRDesignBand();
        header.setHeight(30);
        design.setTitle(header);

        JRDesignStaticText headerSeq = new JRDesignStaticText();
        headerSeq.setX(0);
        headerSeq.setY(5);
        headerSeq.setWidth(50);
        headerSeq.setHeight(20);
        headerSeq.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        headerSeq.setText("Sıra No");
        headerSeq.setMode(ModeEnum.TRANSPARENT);

        header.addElement(headerSeq);

        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(15);

        JRDesignTextField sequenceField = new JRDesignTextField();
        sequenceField.setX(0);
        sequenceField.setY(0);
        sequenceField.setWidth(50);
        sequenceField.setHeight(15);
        sequenceField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        sequenceField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        sequenceField.setMode(ModeEnum.TRANSPARENT);

        sequenceField.setExpression(new JRDesignExpression("$V{REPORT_COUNT}"));

        detailBand.addElement(sequenceField);

        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        detailSection.addBand(detailBand);

        int xPos = 50;
        int columnWidth = 505 / raporIstek.getKolonlar().size();

        for (GnlRaporIstek.RaporKolonDto kolon : raporIstek.getKolonlar()) {

            JRDesignField field = new JRDesignField();
            field.setName(kolon.getAlanAdi());
            design.addField(field);

            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(xPos);
            headerText.setY(5);
            headerText.setWidth(columnWidth);
            headerText.setHeight(15);
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            headerText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            headerText.setText(kolon.getGorunurAdi());

            header.addElement(headerText);

            JRDesignTextField text = new JRDesignTextField();
            text.setX(xPos);
            text.setY(0);
            text.setWidth(columnWidth);
            text.setHeight(15);
            text.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            text.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            text.setExpression(new JRDesignExpression("$F{" + kolon.getAlanAdi() + "}"));
            text.setBlankWhenNull(true);
            text.setPdfEncoding("Cp1254");
            detailBand.addElement(text);

            xPos += columnWidth;
        }

        return design;
    }
//    private JasperDesign createDynamicJasperDesign(GnlRaporIstek raporIstek) throws JRException {
//        JasperDesign design = new JasperDesign();
//        design.setName("DinamikRapor");
//        design.setPageWidth(595);
//        design.setPageHeight(842);
//        design.setColumnWidth(555);
//        design.setLeftMargin(20);
//        design.setRightMargin(20);
//        design.setTopMargin(20);
//        design.setBottomMargin(20);
//
//        design.setQuery(new JRDesignQuery());
//
//        // Title band
//        JRDesignBand titleBand = new JRDesignBand();
//        titleBand.setHeight(50);
//
//        // Title text
//        JRDesignStaticText titleText = new JRDesignStaticText();
//        titleText.setText("Dinamik Rapor - " + raporIstek.getModulAdi());
//        titleText.setX(0);
//        titleText.setY(10);
//        titleText.setWidth(555);
//        titleText.setHeight(20);
//        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
//        titleText.setFontSize(Float.parseFloat("16"));
//        titleText.setBold(true);
//        titleBand.addElement(titleText);
//
//        // Date text
//        JRDesignTextField dateField = new JRDesignTextField();
//        dateField.setExpression(new JRDesignExpression("new java.util.Date()"));
//        dateField.setPattern("dd.MM.yyyy HH:mm");
//        dateField.setX(400);
//        dateField.setY(30);
//        dateField.setWidth(150);
//        dateField.setHeight(15);
//        dateField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
//        titleBand.addElement(dateField);
//
//        design.setTitle(titleBand);
//
//        // Column header band
//        JRDesignBand columnHeaderBand = new JRDesignBand();
//        columnHeaderBand.setHeight(20);
//
//        int xPos = 0;
//        int columnWidth = 555 / raporIstek.getKolonlar().size();
//
//        for (GnlRaporIstek.RaporKolonDto kolon : raporIstek.getKolonlar()) {
//            JRDesignStaticText columnHeader = new JRDesignStaticText();
//            columnHeader.setText(kolon.getGorunurAdi());
//            columnHeader.setX(xPos);
//            columnHeader.setY(0);
//            columnHeader.setWidth(columnWidth);
//            columnHeader.setHeight(20);
//            columnHeader.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
//            columnHeader.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
//            columnHeader.setBackcolor(Color.LIGHT_GRAY);
//            columnHeader.setMode(ModeEnum.OPAQUE);
//            columnHeaderBand.addElement(columnHeader);
//
//            // Add field to design
//            JRDesignField field = new JRDesignField();
//            field.setName(kolon.getAlanAdi());
//            field.setValueClass(getFieldClass(kolon.getVeriTipi()));
//            design.addField(field);
//
//            xPos += columnWidth;
//        }
//
//        design.setColumnHeader(columnHeaderBand);
//
//        // Detail band
////        JRDesignBand detailBand = new JRDesignBand();
////        detailBand.setHeight(20);
////
////        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
////        detailSection.addBand(detailBand);
//        JRDesignBand detailBand = new JRDesignBand();
//        detailBand.setHeight(20);
//
//        // Detail band içine ekle
//        JRDesignTextField sequenceField = new JRDesignTextField();
//        sequenceField.setX(0);                 // ilk kolon
//        sequenceField.setY(0);
//        sequenceField.setWidth(50);            // kolon genişliği
//        sequenceField.setHeight(20);
//        sequenceField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
//        sequenceField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
//        sequenceField.setMode(ModeEnum.TRANSPARENT);
//
//        sequenceField.setExpression(new JRDesignExpression("$V{REPORT_COUNT}"));
//
//        detailBand.addElement(sequenceField);
//
//        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
//        detailSection.addBand(detailBand);
//
//        xPos = 0;
//        for (GnlRaporIstek.RaporKolonDto kolon : raporIstek.getKolonlar()) {
//            JRDesignTextField textField = new JRDesignTextField();
//            textField.setExpression(new JRDesignExpression("$F{" + kolon.getAlanAdi() + "}"));
//            textField.setX(xPos);
//            textField.setY(0);
//            textField.setWidth(columnWidth);
//            textField.setHeight(20);
////            textField.setStretchWithOverflow(true);
//
//            // Format based on data type
//            if ("DATE".equalsIgnoreCase(kolon.getVeriTipi())) {
//                textField.setPattern("dd.MM.yyyy");
//            } else if ("DATETIME".equalsIgnoreCase(kolon.getVeriTipi())) {
//                textField.setPattern("dd.MM.yyyy HH:mm:ss");
//            } else if ("DECIMAL".equalsIgnoreCase(kolon.getVeriTipi()) ||
//                    "DOUBLE".equalsIgnoreCase(kolon.getVeriTipi())) {
//                textField.setPattern("#,##0.00");
//            } else if ("INTEGER".equalsIgnoreCase(kolon.getVeriTipi())) {
//                textField.setPattern("#,##0");
//            }
//
//            detailBand.addElement(textField);
//            xPos += columnWidth;
//        }
//
//        return design;
//    }


    private Map<String, Object> prepareReportParameters(GnlRaporIstek raporIstek) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("REPORT_TITLE", raporIstek.getModulAdi() + " Raporu");
        parameters.put("USER_ID", raporIstek.getKullaniciId());
        parameters.put("REPORT_DATE", LocalDate.now());
        parameters.put("COLUMN_COUNT", raporIstek.getKolonlar().size());

        if (raporIstek.getParametreMap() != null) {
            parameters.putAll(raporIstek.getParametreMap());
        }

        return parameters;
    }


    private byte[] exportReport(JasperPrint jasperPrint, String outputType) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if ("EXCEL".equalsIgnoreCase(outputType)) {
            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(true);
            configuration.setWhitePageBackground(false);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } else if ("PDF".equalsIgnoreCase(outputType)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } else if ("HTML".equalsIgnoreCase(outputType)) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, "rapor.html");
        } else {
            // Default PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }
        return outputStream.toByteArray();
    }


    private Class<?> getFieldClass(String dataType) {
        if (dataType == null) return String.class;

        switch (dataType.toUpperCase()) {
            case "INTEGER":
            case "INT":
                return Integer.class;
            case "LONG":
                return Long.class;
            case "DOUBLE":
            case "DECIMAL":
                return Double.class;
            case "BOOLEAN":
                return Boolean.class;
            case "DATE":
                return LocalDate.class;
            case "DATETIME":
                return LocalDateTime.class;
            default:
                return String.class;
        }
    }
}
