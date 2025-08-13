package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShObeziteAnket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 12:18
 */
@Stateless
public class ShObeziteAnketService extends AbstractService<ShObeziteAnket> {

    @Serial
    private static final long serialVersionUID = 5528169197359526433L;

    public ShObeziteAnketService() {
        super(ShObeziteAnket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
