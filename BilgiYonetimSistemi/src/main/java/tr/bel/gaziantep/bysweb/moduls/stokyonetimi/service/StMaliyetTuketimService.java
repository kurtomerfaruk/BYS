package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StMaliyetTuketim;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 11:11
 */
@Stateless
public class StMaliyetTuketimService extends AbstractService<StMaliyetTuketim> {

    @Serial
    private static final long serialVersionUID = 2734359199108727664L;

    public StMaliyetTuketimService() {
        super(StMaliyetTuketim.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
