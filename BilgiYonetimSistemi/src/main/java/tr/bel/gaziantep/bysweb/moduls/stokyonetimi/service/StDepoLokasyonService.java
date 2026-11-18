package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoLokasyon;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:24
 */
@Stateless
public class StDepoLokasyonService extends AbstractService<StDepoLokasyon> {


    @Serial
    private static final long serialVersionUID = 3065161249981450219L;

    public StDepoLokasyonService() {
        super(StDepoLokasyon.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "ad";
    }

    public List<StDepoLokasyon> findByDepo(StDepo depo) {
        return getEntityManager().createNamedQuery("StDepoLokasyon.findByDepo", StDepoLokasyon.class)
                .setParameter("stDepo", depo)
                .getResultList();
    }
}
