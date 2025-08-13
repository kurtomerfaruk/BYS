package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AySanatsalBeceri;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 14:03
 */
@Stateless
public class AySanatsalBeceriService extends AbstractService<AySanatsalBeceri> {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AySanatsalBeceriService() {
        super(AySanatsalBeceri.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
