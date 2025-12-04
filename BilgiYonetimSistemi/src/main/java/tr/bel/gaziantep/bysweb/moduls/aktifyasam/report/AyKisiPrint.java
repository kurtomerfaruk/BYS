package tr.bel.gaziantep.bysweb.moduls.aktifyasam.report;

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
 * @since 2.07.2025 14:07
 */
@Named
@ViewScoped
@Slf4j
public class AyKisiPrint extends AbstractReportController {

    @Serial
    private static final long serialVersionUID = 8068136413095378720L;
    private static final String PATH = "aktif_yasam";

    public String print(Integer ayKisiId) {
        try {
            setCompileFileName(PATH + File.separator +"aykisi_ziyaretci_tanitim_karti");

            ServletContext sc = Util.getServletContext();
            Map<String,Object> parameterMap = new HashMap<>();
            parameterMap.put("aykisi_id", ayKisiId);
            generateAttribute(sc, parameterMap);
            sc.getRequestDispatcher("/ReportServlet");
            PrimeFaces.current().executeScript("PF('PrintDialog').show()");

        } catch (Exception ex) {
            log.error(null,ex);
        }
        return null;

    }
}
