package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepOdeme;

import java.io.Serial;
import java.util.List;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 14:13
 */
@Stateless
public class OrtTalepOdemeService extends AbstractService<OrtTalepOdeme> {

    @Serial
    private static final long serialVersionUID = -5707029321702870702L;

    public OrtTalepOdemeService() {
        super(OrtTalepOdeme.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtTalepOdeme> findByTalep(OrtTalep talep) {
        if (talep == null || talep.getId() == null) {
            return List.of();
        }
        return getEntityManager().createQuery(
                        "SELECT o FROM OrtTalepOdeme o WHERE o.aktif=true AND o.ortTalep.id=:talepId ORDER BY o.odemeTarihi DESC",
                        OrtTalepOdeme.class)
                .setParameter("talepId", talep.getId())
                .getResultList();
    }

    public boolean existsByTalep(OrtTalep talep) {
        return !findByTalep(talep).isEmpty();
    }
}
