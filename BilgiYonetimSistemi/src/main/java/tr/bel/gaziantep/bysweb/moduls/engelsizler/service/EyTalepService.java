package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:18
 */
@Stateless
public class EyTalepService extends AbstractService<EyTalep> {

    @Serial
    private static final long serialVersionUID = 2937742197686315394L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public EyTalepService() {
        super(EyTalep.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
