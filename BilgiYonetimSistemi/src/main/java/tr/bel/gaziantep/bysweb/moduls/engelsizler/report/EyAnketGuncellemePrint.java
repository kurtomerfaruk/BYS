package tr.bel.gaziantep.bysweb.moduls.engelsizler.report;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractReportController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumRaporTuru;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.18.0
 * @since 10.08.2026 10:39
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class EyAnketGuncellemePrint extends AbstractReportController {
    private static final String PATH = "engelsizler";
    @Serial
    private static final long serialVersionUID = 1124329455375226763L;

    private List<LocalDate> tarih;
    private EnumRaporTuru raporTuru;

    @PostConstruct
    public void init() {
        tarih = new ArrayList<>();
        tarih.add(LocalDate.now());
        tarih.add(LocalDate.now());
    }

    public String print() {
        try {
            setCompileFileName(PATH + File.separator + "eyanket_guncelleme_raporu");
            Map<String, Object> parameterMap = new HashMap<>();
            setExportOption(raporTuru);
            parameterMap.put("baslangicTarihi", DateUtil.localdateToString(tarih.get(0), "dd.MM.yyyy"));
            parameterMap.put("bitisTarihi", DateUtil.localdateToString(tarih.get(1), "dd.MM.yyyy"));

            if (raporTuru == EnumRaporTuru.PDF) {
                ServletContext sc = Util.getServletContext();
                generateAttribute(sc, parameterMap);
                sc.getRequestDispatcher("/ReportServlet");
                PrimeFaces.current().executeScript("PF('PrintDialog').show()");
            } else {
                super.prepareReport(parameterMap);
            }

        } catch (JRException | IOException e) {
            log.error(null, e);
        }
        return null;
    }
}
