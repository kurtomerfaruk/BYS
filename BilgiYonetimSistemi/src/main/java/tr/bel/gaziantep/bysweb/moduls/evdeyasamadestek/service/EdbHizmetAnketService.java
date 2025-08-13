package tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.evdeyasamadestek.entity.EdbHizmetAnket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 10.07.2025 08:25
 */
@Stateless
public class EdbHizmetAnketService extends AbstractService<EdbHizmetAnket> {

    @Serial
    private static final long serialVersionUID = -4967898678172021515L;

    public EdbHizmetAnketService() {
        super(EdbHizmetAnket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
