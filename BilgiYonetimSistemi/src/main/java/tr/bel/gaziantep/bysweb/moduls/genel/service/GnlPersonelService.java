package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 15:31
 */
@Stateless
public class GnlPersonelService extends AbstractService<GnlPersonel> {

    @Serial
    private static final long serialVersionUID = -8216301803042023321L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlPersonelService() {
        super(GnlPersonel.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
