package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyDuyuru;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 09:06
 */
@Stateless
public class SyDuyuruService extends AbstractService<SyDuyuru> {

    @Serial
    private static final long serialVersionUID = 2942989424310155960L;

    public SyDuyuruService() {
        super(SyDuyuru.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SyDuyuru> getTopFive() {
        return getEntityManager().createNamedQuery("SyDuyuru.findAllPublic").setMaxResults(5).getResultList();
    }
}
