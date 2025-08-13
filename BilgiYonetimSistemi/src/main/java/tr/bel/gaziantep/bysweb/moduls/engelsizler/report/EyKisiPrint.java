package tr.bel.gaziantep.bysweb.moduls.engelsizler.report;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 13:20
 */
@Named
@ViewScoped
@Slf4j
public class EyKisiPrint extends AbstractReportController {

    @Serial
    private static final long serialVersionUID = 6440452082993418074L;
    private static final String PATH = "engelsizler";

    public String print(Integer eyKisiId) {
        try {
            setCompileFileName(PATH + File.separator +"eykisi_bilgi_formu");
            Map<String, Object> parameterMap = new HashMap<>();
            setExportOption(EnumRaporTuru.PDF);
            parameterMap.put("kisiId", eyKisiId);
            ServletContext sc = Util.getServletContext();
            generateAttribute(sc, parameterMap);
            sc.getRequestDispatcher("/ReportServlet");
            PrimeFaces.current().executeScript("PF('PrintDialog').show()");
        } catch (Exception ex) {
            log.error(null,ex);
        }
        return null;

    }
}
