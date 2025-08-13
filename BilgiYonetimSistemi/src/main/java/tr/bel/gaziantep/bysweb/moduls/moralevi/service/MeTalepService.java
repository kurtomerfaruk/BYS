package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeTalep;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 11:17
 */
@Stateless
public class MeTalepService extends AbstractService<MeTalep> {

    @Serial
    private static final long serialVersionUID = -9164420896314966L;

    public MeTalepService() {
        super(MeTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
