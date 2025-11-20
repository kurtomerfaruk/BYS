package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeAnket;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 15:06
 */
@Stateless
public class MeAnketService extends AbstractService<MeAnket> {

    @Serial
    private static final long serialVersionUID = -1189614037604839535L;

    public MeAnketService() {
        super(MeAnket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
