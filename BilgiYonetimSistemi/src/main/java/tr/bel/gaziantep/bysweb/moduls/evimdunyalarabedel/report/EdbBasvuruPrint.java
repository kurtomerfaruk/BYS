package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.report;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.12.2025 10:43
 */
@Named
@ViewScoped
@Slf4j
public class EdbBasvuruPrint extends AbstractReportController {
    @Serial
    private static final long serialVersionUID = 1777832167280849897L;
    private static final String PATH = "evim_dunyalara_bedel";

    public String printBasvuru(Integer edbBasvuruId) {
        try {
            setCompileFileName(PATH + File.separator + "edb_basvuru_formu");

            ServletContext sc = Util.getServletContext();
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("edbbasvuru_id", edbBasvuruId);
            generateAttribute(sc, parameterMap);
            sc.getRequestDispatcher("/ReportServlet");
            PrimeFaces.current().executeScript("PF('PrintDialog').show()");

        } catch (Exception ex) {
            log.error(null, ex);
        }
        return null;

    }

    public String printBilgiNotu(Integer edbBasvuruId) {
        try {
            setCompileFileName(PATH + File.separator + "edb_basvuru_bilgi_notu");

            ServletContext sc = Util.getServletContext();
            Map<String, Object> parameterMap = new HashMap<>();
            parameterMap.put("edbbasvuru_id", edbBasvuruId);
            generateAttribute(sc, parameterMap);
            sc.getRequestDispatcher("/ReportServlet");
            PrimeFaces.current().executeScript("PF('PrintDialog').show()");

        } catch (Exception ex) {
            log.error(null, ex);
        }
        return null;

    }
}
