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
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.IOUtils;
import tr.bel.gaziantep.bysweb.core.utils.Constants;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        try {
            @SuppressWarnings("unchecked")
            Map<String,Object> parameters = (Map<String, Object>) session.getAttribute("parameterMap");
            String fileName = (String) session.getAttribute("reportName");

            session.removeAttribute("parameterMap");
            session.removeAttribute("reportName");

            if (StringUtils.isNotBlank(fileName) && parameters!=null && !parameters.isEmpty()) {
//                if (StringUtils.isNotBlank((String) session.getAttribute("persistName"))) {
//                    persistenceName = (String) session.getAttribute("persistName");
//                }

                InitialContext initialContext = new InitialContext();
                DataSource dataSource = (DataSource) initialContext.lookup(Constants.PERSISTENCE_NAME);
                Connection conn = dataSource.getConnection();
                JasperPrint print = JasperFillManager.fillReport(fileName, parameters, conn);

                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline; filename=\"report.pdf\"");
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);

                OutputStream out = response.getOutputStream();
                JasperExportManager.exportReportToPdfStream(print, out);
                response.setContentType("application/pdf");
                response.setContentLength((int) print.getPages().size() * 1024);
                out.flush();
                out.close();

                if (!conn.isClosed()) {
                    conn.close();
                }
            }else{
//                InputStream emptyPdf = getServletContext().getResourceAsStream("/resources/pdf/emptypdf.pdf");
//                OutputStream out = response.getOutputStream();
//                IOUtils.copy(emptyPdf,out);

                sendEmptyPDF(response);
            }
        } catch (NamingException | SQLException | JRException ex) {
            log.error(null,ex);
        }
    }

    private void sendEmptyPDF(HttpServletResponse response) throws IOException {
        try (InputStream emptyPdf = getServletContext().getResourceAsStream("/resources/pdf/emptypdf.pdf")) {
            if (emptyPdf != null) {
                response.setContentType("application/pdf");
                response.setHeader("Content-Disposition", "inline");
                IOUtils.copy(emptyPdf, response.getOutputStream());
            }
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
