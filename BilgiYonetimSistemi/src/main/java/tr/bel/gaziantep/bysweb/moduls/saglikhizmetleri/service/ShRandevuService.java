package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShRandevu;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.8.0
 * @since 21.05.2026 11:25
 */
@Stateless
public class ShRandevuService extends AbstractService<ShRandevu> {

    @Serial
    private static final long serialVersionUID = 1559544407226654965L;

    public ShRandevuService() {
        super(ShRandevu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ShRandevu> findByStartDateByEndDate(LocalDateTime start, LocalDateTime end) {
        return getEntityManager().createNamedQuery("ShRandevu.findByStartDateByEndDate",ShRandevu.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}
