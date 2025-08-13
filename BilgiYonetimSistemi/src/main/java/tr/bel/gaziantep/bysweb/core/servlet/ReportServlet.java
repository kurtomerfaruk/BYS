package tr.bel.gaziantep.bysweb.core.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:17
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/ReportServlet"})
@Slf4j
public class ReportServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -7274124838339720811L;
    private String persistenceName = Constants.PERSISTENCE_NAME;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession(false);

            Map<String,Object> parameters = (Map<String,Object>) session.getAttribute("parameterMap");

            String fileName = (String) session.getAttribute("reportName");

            if(!StringUtil.isBlank(fileName) && !parameters.isEmpty()){
                if (!StringUtil.isBlank((String) session.getAttribute("persistName"))) {
                    persistenceName = (String) session.getAttribute("persistName");
                }

                InitialContext initialContext = new InitialContext();
                DataSource dataSource = (DataSource) initialContext.lookup(persistenceName);
                Connection conn = dataSource.getConnection();

                JasperPrint print = JasperFillManager.fillReport(fileName, parameters, conn);
                byte[] document = JasperExportManager.exportReportToPdf(print);
                OutputStream outputStream = response.getOutputStream();
                response.setContentType("application/pdf");
                response.setContentLength(document.length);
                outputStream.write(document, 0, document.length);

                if (!conn.isClosed()) {
                    conn.close();
                }
            }
        } catch (NamingException | SQLException | JRException ex) {
            log.error(null,ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "ReportServlet";
    }
}
