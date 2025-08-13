package tr.bel.gaziantep.bysweb.moduls.hafriyat.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.HfArac;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.07.2025 11:19
 */
@Stateless
public class HfAracService extends AbstractService<HfArac> {

    @Serial
    private static final long serialVersionUID = -2430141175693632090L;

    public HfAracService() {
        super(HfArac.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
