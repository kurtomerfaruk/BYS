package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlUnvan;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.07.2025 07:58
 */
@Stateless
public class GnlUnvanService extends AbstractService<GnlUnvan> {

    @Serial
    private static final long serialVersionUID = 4771434694148296030L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlUnvanService() {
        super(GnlUnvan.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
