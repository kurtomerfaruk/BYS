package tr.bel.gaziantep.bysweb.moduls.engelsizler.report;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 13:54
 */
@Named
@ViewScoped
@Slf4j
public class EyAracTamirPrint extends AbstractReportController {

    @Serial
    private static final long serialVersionUID = 216828311473217640L;
    private static final String PATH = "engelsizler";

    public String print(Integer id) {
        try {
            setCompileFileName(PATH + File.separator +"eyarac_tamir_formu");

            ServletContext sc = Util.getServletContext();
            Map<String,Object> parameterMap = new HashMap<>();
            parameterMap.put("tamirId", id);
            generateAttribute(sc, parameterMap);
            sc.getRequestDispatcher("/ReportServlet");

        } catch (Exception ex) {
            log.error(null,ex);
        }
        return null;
    }
}
