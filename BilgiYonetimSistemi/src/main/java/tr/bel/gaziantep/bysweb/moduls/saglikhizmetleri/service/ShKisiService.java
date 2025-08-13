package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 16:19
 */
@Stateless
public class ShKisiService extends AbstractService<ShKisi> {

    @Serial
    private static final long serialVersionUID = 1059287938548863003L;

    public ShKisiService() {
        super(ShKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public boolean existByGnlKisiByShKisi(ShKisi shKisi) {
        List<ShKisi> result = getEntityManager().createNamedQuery("ShKisi.existByGnlKisiByShKisi")
                .setParameter("gnlKisi", shKisi.getGnlKisi())
                .setParameter("id", shKisi.getId())
                .getResultList();
        return result.size() > 0;
    }

    public boolean existByGnlKisi(GnlKisi gnlKisi) {
        List<ShKisi> result = getEntityManager().createNamedQuery("ShKisi.existByGnlKisi")
                .setParameter("gnlKisiId", gnlKisi.getId())
                .getResultList();
        return result.size() > 0;
    }
}
