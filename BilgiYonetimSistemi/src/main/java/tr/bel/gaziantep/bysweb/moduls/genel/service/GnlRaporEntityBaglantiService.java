package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporEntityBaglanti;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 11:48
 */
@Stateless
public class GnlRaporEntityBaglantiService extends AbstractService<GnlRaporEntityBaglanti> {

    @Serial
    private static final long serialVersionUID = 8122040966697680232L;

    public GnlRaporEntityBaglantiService() {
        super(GnlRaporEntityBaglanti.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
