package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:03
 */
@Stateless
public class OrtRandevuService extends AbstractService<OrtRandevu> {

    @Serial
    private static final long serialVersionUID = 7671048555616620624L;

    public OrtRandevuService() {
        super(OrtRandevu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtRandevu> findByStartDateByEndDate(LocalDateTime start, LocalDateTime end) {
        return getEntityManager().createNamedQuery("OrtRandevu.findByStartDateByEndDate")
                .setParameter("start",start)
                .setParameter("end",end)
                .getResultList();
    }
}
