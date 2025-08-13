package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlikResim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 14:09
 */
@Stateless
public class MeEtkinlikResimService extends AbstractService<MeEtkinlikResim> {

    @Serial
    private static final long serialVersionUID = -3685871644283090377L;

    public MeEtkinlikResimService() {
        super(MeEtkinlikResim.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
