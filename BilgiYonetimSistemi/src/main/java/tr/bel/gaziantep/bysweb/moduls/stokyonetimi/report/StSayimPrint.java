package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.report;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 24.11.2026 09:39
 */
@Named
@ViewScoped
@Slf4j
public class StSayimPrint extends AbstractReportController {
    @Serial
    private static final long serialVersionUID = 5323649336650122429L;
    private static final String PATH = "stok_yonetimi";

    public String print(Integer sayimId) {
        try {
            setCompileFileName(PATH + File.separator + "sayim_listesi");
            Map<String, Object> parameterMap = new HashMap<>();
            setExportOption(EnumRaporTuru.PDF);
            parameterMap.put("sayimId", sayimId);
            ServletContext sc = Util.getServletContext();
            generateAttribute(sc, parameterMap);
        } catch (Exception ex) {
            log.error(null, ex);
        }
        return null;

    }
}
