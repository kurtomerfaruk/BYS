package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:58
 */
@Stateless
public class StStokBarkodService extends AbstractService<StStokBarkod> {

    @Serial
    private static final long serialVersionUID = 8571060977864743499L;

    public StStokBarkodService() {
        super(StStokBarkod.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StStokBarkod findByBarkod(String barkod) {
        if (barkod == null || barkod.isBlank()) {
            return null;
        }
        return em.createQuery("SELECT b FROM StStokBarkod b WHERE b.barkod = :barkod AND b.aktif = true",
                        StStokBarkod.class)
                .setParameter("barkod", barkod.trim())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

}
