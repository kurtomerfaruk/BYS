package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.ReportConfigUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:21
 */
@Setter
@Slf4j
public abstract class AbstractReportController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = -7585964005572221299L;

    private EnumRaporTuru exportOption;
    private static final String COMPILE_DIR = "/reportFiles";

    @Getter
    private String message;
    @Getter
    private String compileFileName;

    public AbstractReportController() {
        super();
    }

    protected void prepareReport(Map<String, Object> params) throws JRException, IOException {
        ServletContext context = Util.getServletContext();

        ReportConfigUtil.compileReport(getCompileDir(), getCompileFileName());
        String fileName = context.getRealPath(getCompileDir()) + File.separator + getCompileFileName() + ".jasper";
        File reportFile = new File(fileName);

        Connection conn = null;
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(Constants.PERSISTENCE_NAME);
            conn = dataSource.getConnection();
        } catch (NamingException | SQLException ex) {
           log.error(null,ex);
        }

        String reportPath = context.getRealPath(COMPILE_DIR) + File.separator;
        params.put("SUBREPORT_DIR", reportPath);

        JasperPrint jasperPrint = ReportConfigUtil.fillReport(reportFile, params, conn);

        switch (getExportOption()) {
            case XLS:
                ReportConfigUtil.exportReportAsExcel(jasperPrint, getCompileFileName());
                break;
            case PPT:
                ReportConfigUtil.exportReportAsPPT(jasperPrint, getCompileFileName());
                break;
            case DOCX:
                ReportConfigUtil.exportReportAsDOCX(jasperPrint, getCompileFileName());
                break;
            default:
                break;
        }

        FacesContext.getCurrentInstance().responseComplete();
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException ex) {
            log.error(null,ex);
        }
    }

    public EnumRaporTuru getExportOption() {
        if (exportOption == null) {
            exportOption = EnumRaporTuru.PDF;

        }
        return exportOption;
    }

    protected String getCompileDir() {
        return COMPILE_DIR;
    }

    protected void generateAttribute(ServletContext sc, Map<String, Object> parameterMap) {

        String reportPath = ReportConfigUtil.getJasperFileDir(sc, getCompileDir());
        String reportName = reportPath + File.separator + getCompileFileName() + ".jasper";
        HttpServletRequest request = Util.getRequest();
        HttpSession session = request.getSession();
        parameterMap.put("SUBREPORT_DIR", reportPath);

        session.setAttribute("reportName", reportName);
        session.setAttribute("parameterMap", parameterMap);
        session.setAttribute("persistName", Constants.PERSISTENCE_NAME);
    }

}
