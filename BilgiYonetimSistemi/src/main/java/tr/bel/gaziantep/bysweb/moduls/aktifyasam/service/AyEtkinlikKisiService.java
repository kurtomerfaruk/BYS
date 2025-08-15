package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlikKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 15:25
 */
@Stateless
public class AyEtkinlikKisiService extends AbstractService<AyEtkinlikKisi> {
    @Serial
    private static final long serialVersionUID = -8130013426383481162L;

    public AyEtkinlikKisiService() {
        super(AyEtkinlikKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyEtkinlikKisi> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("AyEtkinlikKisi.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
