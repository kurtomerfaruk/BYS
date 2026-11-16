package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransfer;

import java.io.Serial;
import java.time.Year;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:08
 */
@Stateless
public class StTransferService extends AbstractService<StTransfer> {

    @Serial
    private static final long serialVersionUID = -418315528848623451L;

    public StTransferService() {
        super(StTransfer.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "talepTarihi";
    }

    public String generateTransferNo() {
        int year = Year.now().getValue();
        String prefix = year + "/";
        jakarta.persistence.TypedQuery<String> q = em.createQuery(
                "SELECT t.transferNo FROM StTransfer t WHERE t.transferNo LIKE :prefix", String.class);
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
