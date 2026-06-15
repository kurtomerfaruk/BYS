package tr.bel.gaziantep.bysweb.moduls.ileriyas.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.10.0
 * @since 11.06.2026 08:38
 */
@Stateless
public class IyKisiService extends AbstractService<IyKisi> {

    @Serial
    private static final long serialVersionUID = 1263207619158334284L;

    public IyKisiService() {
        super(IyKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IyKisi findByTcKimlikNo(String  tcKimlikNo) {
        return getEntityManager().createNamedQuery("IyKisi.findByTcKimlikNo",IyKisi.class)
                .setParameter("tcKimlikNo",tcKimlikNo)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
