package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import com.kurtomerfaruk.amchartfaces.model.ChartData;
import com.kurtomerfaruk.amchartfaces.model.ChartDataThreeColumn;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGrafik;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 27.04.2026 09:34
 */
@Stateless
public class SyGrafikService extends AbstractService<SyGrafik> {

    @Serial
    private static final long serialVersionUID = 1119329941866491299L;

    public SyGrafikService() {
        super(SyGrafik.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SyGrafik> findByModul(EnumModul modul) {
        return getEntityManager().createNamedQuery("SyGrafik.findByModul",SyGrafik.class)
                .setParameter("modul",modul)
                .getResultList();
    }

    public List<ChartData> executeQuery(String sorgu) {
        return getEntityManager().createNativeQuery(sorgu, "GrafikData").getResultList();
    }

    public List<ChartDataThreeColumn> executeQueryV2(String sorgu) {
        return getEntityManager().createNativeQuery(sorgu, "GrafikData2").getResultList();
    }
}
