package tr.bel.gaziantep.bysweb.moduls.genel.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.06.2025 08:31
 */
@Stateless
public class GnlIlService extends AbstractService<GnlIl> {

    @Serial
    private static final long serialVersionUID = -7441881617565584801L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public GnlIlService() {
        super(GnlIl.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GnlIl findByKod(Integer kod) {
        return (GnlIl) getEntityManager().createNamedQuery("GnlIl.findByKod")
                .setParameter("kod",kod)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }
}
