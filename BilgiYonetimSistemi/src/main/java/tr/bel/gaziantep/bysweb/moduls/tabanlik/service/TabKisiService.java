package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 10:19
 */
@Stateless
public class TabKisiService extends AbstractService<TabKisi> {

    @Serial
    private static final long serialVersionUID = 4264738578214254555L;

    public TabKisiService() {
        super(TabKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TabKisi findByTcKimlikNo(String tcKimlikNo) {
        List<TabKisi> result = getEntityManager()
                .createNamedQuery("TabKisi.findByTcKimlikNo", TabKisi.class)
                .setParameter("tcKimlikNo", tcKimlikNo)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}
