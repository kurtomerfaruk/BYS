package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;

import java.io.Serial;
import java.time.Year;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 08:47
 */
@Stateless
public class StStokHareketService extends AbstractService<StStokHareket> {

    @Serial
    private static final long serialVersionUID = -7182489919506157446L;

    public StStokHareketService() {
        super(StStokHareket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "islemTarihi";
    }

    public String generateHareketNo() {
        int year = Year.now().getValue();
        String prefix = year + "/";
        jakarta.persistence.TypedQuery<String> q = em.createQuery(
                "SELECT h.hareketNo FROM StStokHareket h WHERE h.hareketNo LIKE :prefix", String.class);
        q.setParameter("prefix", prefix + "%");
        int max = 0;
        for (String no : q.getResultList()) {
            String seqPart = no.substring(prefix.length());
            try {
                int val = Integer.parseInt(seqPart);
                if (val > max) max = val;
            } catch (NumberFormatException ignored) {
            }
        }
        return prefix + String.format("%06d", max + 1);
    }
}
