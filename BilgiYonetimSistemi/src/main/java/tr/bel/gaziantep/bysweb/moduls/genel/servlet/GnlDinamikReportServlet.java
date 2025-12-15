package tr.bel.gaziantep.bysweb.moduls.genel.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 12.12.2025 15:23
 */
@WebServlet("/gnlDinamikReportServlet")
public class GnlDinamikReportServlet extends HttpServlet {


    @Serial
    private static final long serialVersionUID = -7465511854306894557L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();
        byte[] data = (byte[]) session.getAttribute("pdfData");

        if (data == null) {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "inline;filename=rapor.pdf");

        OutputStream out = resp.getOutputStream();
        out.write(data);
        out.flush();
        out.close();

        session.removeAttribute("pdfData");
    }
}
