package tr.bel.gaziantep.bysweb.core.utils;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.poi.export.JRXlsExporter;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.sql.Connection;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:24
 */
public class ReportConfigUtil implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = -718253070093761982L;

    private static void setCompileTempDir(ServletContext context, String uri) {
        System.setProperty("jasper.reports.compile.temp", context.getRealPath(uri));
    }

    public static boolean compileReport(String compileDir, String filename) throws JRException {
        ServletContext context = Util.getServletContext();
        String jasperFileName = context.getRealPath(compileDir) + File.separator + filename + ".jasper";
        File jasperFile = new File(jasperFileName);

        if (jasperFile.exists()) {
            return true;
        }
        try {
            setCompileTempDir(context, compileDir);

            String xmlFileName = jasperFileName.substring(0, jasperFileName.indexOf(".jasper")) + ".jrxml";
            JasperCompileManager.compileReportToFile(xmlFileName);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JasperPrint fillReport(File reportFile, Map<String, Object> parameters, Connection conn) throws JRException {
        parameters.put("BaseDir", reportFile.getParentFile());

        JasperPrint jasperPrint = JasperFillManager.fillReport(reportFile.getPath(), parameters, conn);

        return jasperPrint;
    }

    public static String getJasperFilePath(ServletContext context, String compileDir, String jasperFile) {
        return context.getRealPath(compileDir) + jasperFile;
    }

    public static String getJasperFileDir(ServletContext context, String compileDir) {
        return context.getRealPath(compileDir) + File.separator;
    }

    public static String getJasperFileDir(ExternalContext context, String compileDir) {
        return context.getRealPath(compileDir) + File.separator;
    }

    private static void exportReport(JRAbstractExporter exporter, JasperPrint jasperPrint, PrintWriter out) throws JRException {
        exporter.exportReport();
    }


    public static void exportReportAsExcel(JasperPrint jasperPrint, String fileName) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRXlsxExporter xlsxExporter = new JRXlsxExporter();
        xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));
        xlsxExporter.exportReport();

    }

    public static void exportReportAsPPT(JasperPrint jasperPrint, String fileName) throws JRException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".pptx");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRPptxExporter ppt = new JRPptxExporter();
        ppt.setExporterInput(new SimpleExporterInput(jasperPrint));
        ppt.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));
        ppt.exportReport();
    }

    public static void exportReportAsDOCX(JasperPrint jasperPrint, String fileName) throws IOException, JRException {
        HttpServletResponse httpServletResponse =  Util.getResponse();
        httpServletResponse.addHeader("Content-disposition", "attachment; filename=" + fileName + ".docx");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        JRDocxExporter docxExporter = new JRDocxExporter();
        docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        docxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(servletOutputStream));
        docxExporter.exportReport();
    }

    public static void directPrint(JasperPrint jasperPrint) throws JRException {
        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        if (defaultPrintService == null) {
            System.out.println("Varsayılan bir yazıcı bulunamadı!");
            return;
        }
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        printServiceAttributeSet.add(new PrinterName(defaultPrintService.getName(), null));
        SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
        configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
        configuration.setDisplayPageDialog(false);
        configuration.setDisplayPrintDialog(false);

        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }
}
