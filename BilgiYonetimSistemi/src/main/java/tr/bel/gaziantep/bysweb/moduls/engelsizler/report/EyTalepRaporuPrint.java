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
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;

import java.io.File;
import java.io.IOException;
import java.io.Serial;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 09:08
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class EyTalepRaporuPrint extends AbstractReportController {
    @Serial
    private static final long serialVersionUID = 8108989016737227335L;
    private static final String PATH = "engelsizler";
    private static final String DATE_FORMAT = "dd.MM.yyyy";

    private EnumRaporTuru raporTuru;
    private GnlIlce gnlIlce;
    private GnlMahalle gnlMahalle;
    private LocalDate baslangicTarihi;
    private LocalDate bitisTarihi;
    private EyTalepKonu eyTalepKonu;
    private EnumGnlTalepDurumu durum;

    @PostConstruct
    public void init(){
        bitisTarihi = LocalDate.now();
        baslangicTarihi = bitisTarihi.minusDays(7);
    }

    public String print() {
        try {
            setCompileFileName(PATH + File.separator + "eytalep_rapor");
            Map<String, Object> parameterMap = new HashMap<>();
            setExportOption(raporTuru);
            parameterMap.put("baslangicTarihi", DateUtil.localdateToString(baslangicTarihi, DATE_FORMAT));
            parameterMap.put("bitisTarihi", DateUtil.localdateToString(bitisTarihi, DATE_FORMAT));
            parameterMap.put("mahalleId", gnlMahalle == null ? -1 : gnlMahalle.getId());
            parameterMap.put("ilceId", gnlIlce == null ? -1 : gnlIlce.getId());
            parameterMap.put("konuId", eyTalepKonu == null ? -1 : eyTalepKonu.getId());
            parameterMap.put("durum", durum == null ? -1 : durum.name());


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
