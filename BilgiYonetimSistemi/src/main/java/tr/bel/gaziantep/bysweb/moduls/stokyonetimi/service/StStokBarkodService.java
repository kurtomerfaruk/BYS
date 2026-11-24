package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;

import java.io.Serial;
import java.util.List;

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

    @Override
    public void create(StStokBarkod entity) {
        if (entity != null && (entity.getBarkod() == null || entity.getBarkod().isBlank())) {
            entity.setBarkod(getNextBarkod());
        }
        super.create(entity);
    }

    public String getNextBarkod() {
        List<String> codes = em.createQuery(
                        "SELECT b.barkod FROM StStokBarkod b WHERE b.barkod IS NOT NULL AND b.barkod <> '' ORDER BY b.id DESC",
                        String.class)
                .setMaxResults(200)
                .getResultList();
        long maxSeq = 0;
        for (String code : codes) {
            if (code == null) continue;
            String trimmed = code.trim();
            if (trimmed.matches("869\\d{10}")) {
                try {
                    long seq = Long.parseLong(trimmed.substring(3, 12));
                    if (seq > maxSeq) {
                        maxSeq = seq;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return buildEan13(maxSeq + 1);
    }

    public String incrementBarkod(String barkod) {
        if (barkod == null || !barkod.trim().matches("869\\d{10}")) {
            return getNextBarkod();
        }
        try {
            long seq = Long.parseLong(barkod.trim().substring(3, 12));
            return buildEan13(seq + 1);
        } catch (NumberFormatException e) {
            return getNextBarkod();
        }
    }

    private String buildEan13(long sequence) {
        String body = "869" + String.format("%09d", sequence);
        int checkDigit = calculateEanCheckDigit(body);
        return body + checkDigit;
    }

    private int calculateEanCheckDigit(String twelveDigits) {
        int sum = 0;
        for (int i = 0; i < 12 && i < twelveDigits.length(); i++) {
            int digit = Character.getNumericValue(twelveDigits.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int mod = sum % 10;
        return (10 - mod) % 10;
    }

}
