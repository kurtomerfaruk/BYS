package tr.bel.gaziantep.bysweb.moduls.engelsizler.report;

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
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 10:23
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class EyGenelPrint extends AbstractReportController {

    @Serial
    private static final long serialVersionUID = 6998535964949617041L;
    private static final String PATH = "engelsizler";

    private EnumRaporTuru raporTuru;
    private GnlIlce gnlIlce;
    private GnlMahalle gnlMahalle;
    private int yasBaslangic;
    private int yasBitis;
    private EnumGnlDurum durum;
    private EyEngelGrubu eyEngelGrubu;

    public String print() {
        try {
            setCompileFileName(PATH + File.separator +"eygenel_rapor");
            Map<String, Object> parameterMap = new HashMap<>();

            int mahalleId = gnlMahalle == null ? -1 : gnlMahalle.getId();
            int ilceId = gnlIlce == null ? -1 : gnlIlce.getId();
            int engelGrubuId = eyEngelGrubu == null ? -1 : eyEngelGrubu.getId();

            parameterMap.put("mahalleId", mahalleId);
            parameterMap.put("ilceId", ilceId);
            parameterMap.put("engelGrubuId", engelGrubuId);
            parameterMap.put("durum", durum.name());
            setExportOption(raporTuru);

            if (raporTuru == EnumRaporTuru.PDF) {
                ServletContext sc = Util.getServletContext();
                generateAttribute(sc, parameterMap);
                sc.getRequestDispatcher("/ReportServlet");
                PrimeFaces.current().executeScript("PF('PrintDialog').show()");
            } else {
                super.prepareReport(parameterMap);
            }

        } catch (JRException | IOException e) {
           log.error(null,e);
        }
        return null;
    }
}
