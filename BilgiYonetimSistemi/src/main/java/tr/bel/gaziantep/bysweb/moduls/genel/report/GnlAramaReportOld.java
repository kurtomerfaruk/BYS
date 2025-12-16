package tr.bel.gaziantep.bysweb.moduls.genel.report;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
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
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlAramaModul;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.12.2025 14:21
 */
@Named
@ViewScoped
@Slf4j
public class GnlAramaReportOld implements Serializable {
    @Serial
    private static final long serialVersionUID = -2000288334750026244L;

    @Inject
    private GnlKisiService gnlKisiService;

    @Getter
    @Setter
    private EnumGnlAramaModul aramaModul;
    @Getter
    @Setter
    private GnlIlce gnlIlce;
    @Getter
    @Setter
    private GnlMahalle gnlMahalle;
    @Getter
    @Setter
    private Integer minYas;
    @Getter
    @Setter
    private Integer maxYas;
    @Getter
    @Setter
    private boolean sag;
    @Getter
    @Setter
    private EyEngelGrubu engelGrubu;

    @PostConstruct
    public void init() {
        minYas = 0;
        maxYas = 100;
        sag = true;
    }

    public void print() {
        try {
            List<GnlKisi> kisiler = gnlKisiService.findByLatLngIsNull(10);

            JasperPrint print = buildReport("",kisiler);

            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            ec.responseReset();
            ec.setResponseContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            ec.setResponseHeader("Content-Disposition", "attachment; filename=\"rapor.xlsx\"");


            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(print));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ec.getResponseOutputStream()));

            // Optional: export options
            SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setCollapseRowSpan(true);
            configuration.setWhitePageBackground(false);
            exporter.setConfiguration(configuration);

            exporter.exportReport();

            fc.responseComplete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public String buildQuery(String type) {

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT K.AD,K.SOYAD ");
        sql.append("FROM GNLKISI K");


        return sql.toString();
    }

    public JasperPrint buildReport(String reportType, List<GnlKisi> kisiler) throws JRException {

        List<DynamicColumn> columns = buildColumns(reportType);
        List<Map<String,Object>> data = buildData(reportType, kisiler);

        JRDataSource dataSource = new JRMapCollectionDataSource((Collection<Map<String, ?>>)(Collection<?>) data);

        JasperDesign design = new JasperDesign();
        design.setName("dynamic_report");
        design.setPageWidth(595);
        design.setPageHeight(842);
        design.setColumnWidth(555);
        design.setLeftMargin(20);
        design.setRightMargin(20);
        design.setTopMargin(20);
        design.setBottomMargin(20);

        // Query optional for BeanCollectionDataSource
        design.setQuery(new JRDesignQuery()); // boş query

        // Column header band
        JRDesignBand header = new JRDesignBand();
        header.setHeight(30);
        design.setColumnHeader(header);

        // Detail band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(15);

        // Detail band içine ekle
        JRDesignTextField sequenceField = new JRDesignTextField();
        sequenceField.setX(0);                 // ilk kolon
        sequenceField.setY(0);
        sequenceField.setWidth(50);            // kolon genişliği
        sequenceField.setHeight(15);
        sequenceField.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
        sequenceField.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
        sequenceField.setMode(ModeEnum.TRANSPARENT);

        sequenceField.setExpression(new JRDesignExpression("$V{REPORT_COUNT}"));

        detailBand.addElement(sequenceField);

        JRDesignSection detailSection = (JRDesignSection) design.getDetailSection();
        detailSection.addBand(detailBand);

        int x = 50;

        for (DynamicColumn col : columns) {

            // 1) Field
            JRDesignField field = new JRDesignField();
            field.setName(col.getField());
            field.setValueClass((Class<?>) col.getClazz());
            design.addField(field);

            // 2) Header
            JRDesignStaticText headerText = new JRDesignStaticText();
            headerText.setX(x);
            headerText.setY(5);
            headerText.setWidth(col.getWidth());
            headerText.setHeight(15);
            headerText.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            headerText.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            headerText.setText(col.getTitle());

            header.addElement(headerText);

            // 3) Detail
            JRDesignTextField text = new JRDesignTextField();
            text.setX(x);
            text.setY(0);
            text.setWidth(col.getWidth());
            text.setHeight(15);
            text.setHorizontalTextAlign(HorizontalTextAlignEnum.LEFT);
            text.setVerticalTextAlign(VerticalTextAlignEnum.MIDDLE);
            text.setExpression(new JRDesignExpression("$F{" + col.getField() + "}"));
            text.setBlankWhenNull(true);
            detailBand.addElement(text);

            x += col.getWidth();
        }

        JasperReport report = JasperCompileManager.compileReport(design);
        return JasperFillManager.fillReport(report, new HashMap<>(), dataSource);
    }

    private List<DynamicColumn> buildColumns(String reportType) {
        List<DynamicColumn> cols = new ArrayList<>();
        cols.add(new DynamicColumn("tcKimlikNo", "T.C.Kimlik No", 120,String.class));
        cols.add(new DynamicColumn("ad", "Ad", 120,String.class));
        cols.add(new DynamicColumn("soyad", "Soyad", 120,String.class));
        cols.add(new DynamicColumn("dogumTarihi", "Doğum Tarihi", 120, LocalDate.class));
        cols.add(new DynamicColumn("dogumYeri", "Doğum Yeri", 120,String.class));
        cols.add(new DynamicColumn("anaAdi", "Ana Adı", 120,String.class));
        cols.add(new DynamicColumn("babaAdi", "Baba Adı", 120,String.class));
        cols.add(new DynamicColumn("telefon", "Telefon", 120,String.class));

//        if ("ENGELLI".equals(reportType)) {
//            cols.add(new DynamicColumn("engelDurumu", "Engel Durumu", 150));
//            cols.add(new DynamicColumn("engelOrani", "Engel Oranı", 80));
//        }
//
//        if ("MORAL_EVI".equals(reportType)) {
//            cols.add(new DynamicColumn("programAdi", "Program", 150));
//            cols.add(new DynamicColumn("katilimTarihi", "Katılım Tarihi", 100));
//        }

        return cols;
    }

    /**
     * Data maplerini rapor tipine göre oluşturur
     */
    private List<Map<String,Object>> buildData(String reportType, List<GnlKisi> kisiler) {
        List<Map<String,Object>> data = new ArrayList<>();

        for (GnlKisi k : kisiler) {
            Map<String,Object> row = new HashMap<>();
            row.put("tcKimlikNo",k.getTcKimlikNo());
            row.put("ad", k.getAd());
            row.put("soyad", k.getSoyad());
            row.put("dogumTarihi", k.getDogumTarihi());
            row.put("dogumYeri", k.getDogumYeri());
            row.put("anaAdi", k.getAnaAdi());
            row.put("babaAdi", k.getBabaAdi());
            row.put("telefon", k.getTelefon());
            data.add(row);
        }

        return data;
    }
}
