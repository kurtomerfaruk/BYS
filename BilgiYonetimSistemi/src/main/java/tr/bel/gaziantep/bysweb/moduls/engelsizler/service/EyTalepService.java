package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:18
 */
@Stateless
public class EyTalepService extends AbstractService<EyTalep> {

    @Serial
    private static final long serialVersionUID = 2937742197686315394L;

    public EyTalepService() {
        super(EyTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<EyTalep> findByDurumByOlu(EnumGnlTalepDurumu durum) {
        return getEntityManager().createNamedQuery("EyTalep.findByDurumByOlu", EyTalep.class).setParameter("durum", durum).getResultList();
    }

    public List<EyTalep> findByDurum(EnumGnlTalepDurumu durum) {
        return getEntityManager().createNamedQuery("EyTalep.findByDurum", EyTalep.class).setParameter("durum", durum).getResultList();
    }
}
