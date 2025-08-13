package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlik;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.07.2025 10:18
 */
@Stateless
public class AyEtkinlikService extends AbstractService<AyEtkinlik> {

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public AyEtkinlikService() {
        super(AyEtkinlik.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
