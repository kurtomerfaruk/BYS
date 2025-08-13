package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbEkip;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:55
 */
@Stateless
public class EdbEkipService extends AbstractService<EdbEkip> {

    @Serial
    private static final long serialVersionUID = -5628359694546303005L;

    public EdbEkipService() {
        super(EdbEkip.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
