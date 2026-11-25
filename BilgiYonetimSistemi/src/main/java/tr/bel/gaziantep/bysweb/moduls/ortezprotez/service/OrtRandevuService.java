package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtRandevu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 08:31
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

    public List<OrtRandevu> findByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return getEntityManager().createNamedQuery("OrtRandevu.findByTalep", OrtRandevu.class)
                .setParameter("ortTalep", talep)
                .getResultList();
    }

    public List<OrtRandevu> findByTalepByType(OrtTalep talep, EnumOrtRandevuTuru randevuTuru) {
        return findByTalep(talep).stream()
                .filter(r -> r.getRandevuTuru() == randevuTuru)
                .toList();
    }

    public List<OrtRandevu> findByDateRange(LocalDateTime start, LocalDateTime end) {
        return getEntityManager().createNamedQuery("OrtRandevu.findByStartDateByEndDate", OrtRandevu.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    /**
     * Aynı talep ve tipte planlanmış (henüz gerçekleşmemiş) randevu var mı?
     */
    public boolean existsPlanned(OrtTalep talep, EnumOrtRandevuTuru randevuTuru) {
        return findByTalepByType(talep, randevuTuru).stream()
                .anyMatch(r -> r.getDurum() == tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtRandevuDurumu.PLANLANDI);
    }

    /**
     * Randevunun hasta tarafından gerçekleştirilip gerçekleştirilmediğini kontrol eder.
     */
    public boolean existsAttended(OrtTalep talep, EnumOrtRandevuTuru randevuTuru) {
        return findByTalepByType(talep, randevuTuru).stream()
                .anyMatch(r -> r.getDurum() == EnumOrtRandevuDurumu.GELDI
                        || r.getDurum() == EnumOrtRandevuDurumu.TAMAMLANDI);
    }
}
