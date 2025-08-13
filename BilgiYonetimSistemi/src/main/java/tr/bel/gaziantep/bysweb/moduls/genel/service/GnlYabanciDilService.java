package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlYabanciDil;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 08:27
 */
@Stateless
public class GnlYabanciDilService extends AbstractService<GnlYabanciDil> {

    @Serial
    private static final long serialVersionUID = 4303300151292552380L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlYabanciDilService() {
        super(GnlYabanciDil.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
