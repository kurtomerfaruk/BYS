package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyCozger;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 11:36
 */
@Stateless
public class EyCozgerService extends AbstractService<EyCozger> {

    @Serial
    private static final long serialVersionUID = 444384600025739999L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyCozgerService() {
        super(EyCozger.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
