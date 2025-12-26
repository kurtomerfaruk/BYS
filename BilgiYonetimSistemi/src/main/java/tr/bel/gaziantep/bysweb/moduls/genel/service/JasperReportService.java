package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.VerticalTextAlignEnum;
import net.sf.jasperreports.export.*;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.dtos.GnlRaporDto;
import tr.bel.gaziantep.bysweb.moduls.genel.dtos.GnlRaporKolonDto;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
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

    public byte[] generateReport(List<Map<String, Object>> data, GnlRaporDto raporIstek) {
        try {
            JasperDesign jasperDesign = createDynamicJasperDesign(raporIstek, data);

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String, Object> parameters = prepareReportParameters(raporIstek);
            JRDataSource dataSource = new JRMapCollectionDataSource((Collection<Map<String, ?>>) (Collection<?>) data);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return exportReport(jasperPrint, raporIstek.getRaporTuru());

        } catch (JRException e) {
            throw new RuntimeException("JasperReport hatası: " + e.getMessage(), e);
        }
    }


    private JasperDesign createDynamicJasperDesign(GnlRaporDto raporIstek, List<Map<String, Object>> data) throws JRException {

        JasperDesign design = new JasperDesign();
        design.setName("dynamic_report");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(555);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        design.setQuery(new JRDesignQuery());

        JRDesignBand header = new JRDesignBand();
        header.setHeight(30);
        design.setTitle(header);

        JRDesignStaticText headerSeq = new JRDesignStaticText();
        headerSeq.setX(0);
        headerSeq.setY(5);
        headerSeq.setWidth(50);
        headerSeq.setHeight(15);
        headerSeq.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        headerSeq.setText("Sıra No");
        headerSeq.setMode(ModeEnum.TRANSPARENT);
        headerSeq.setPdfEncoding(Constants.CP1254);
        addLine(headerSeq);

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

        addLine(sequenceField);

        sequenceField.setExpression(new JRDesignExpression("$V{REPORT_COUNT}"));

        detailBand.addElement(sequenceField);

        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        detailSection.addBand(detailBand);

        int xPos = 50;
        int columnWidth = 505 / raporIstek.getKolonlar().size();
        Map<String, Object> val = data.get(0);
        for (GnlRaporKolonDto kolon : raporIstek.getKolonlar()) {

            JRDesignField field = new JRDesignField();
            field.setName(kolon.getAlanAdi());
            field.setValueClass(val.get(kolon.getAlanAdi()).getClass());
            design.addField(field);

            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(xPos);
            headerText.setY(5);
            headerText.setWidth(columnWidth);
            headerText.setHeight(15);
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            headerText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            headerText.setText(kolon.getGorunurAdi());
            headerText.setPdfEncoding(Constants.CP1254);

            addLine(headerText);

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
            text.setPdfEncoding(Constants.CP1254);

            addLine(text);

            detailBand.addElement(text);

            xPos += columnWidth;
        }

        return design;
    }

    public void addLine(JRDesignTextField text) {
        JRLineBox detailBox = text.getLineBox();
        detailBox.getPen().setLineWidth(0.5f);
    }

    public void addLine(JRDesignStaticText text) {
        JRLineBox detailBox = text.getLineBox();
        detailBox.getPen().setLineWidth(0.5f);
    }

    private Map<String, Object> prepareReportParameters(GnlRaporDto raporIstek) {
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


    private byte[] exportReport(JasperPrint jasperPrint, EnumRaporTuru outputType) throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        if (outputType.equals(EnumRaporTuru.XLS)) {
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
        } else if (outputType.equals(EnumRaporTuru.CSV)) {
            JRCsvExporter exporter = new JRCsvExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
            SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
            configuration.setWriteBOM(Boolean.TRUE);
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } else {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }
        return outputStream.toByteArray();
    }
}
