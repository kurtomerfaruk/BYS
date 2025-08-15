package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlikKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 15:01
 */
@Stateless
public class MeEtkinlikKisiService extends AbstractService<MeEtkinlikKisi> {

    @Serial
    private static final long serialVersionUID = -5689848816114452384L;

    public MeEtkinlikKisiService() {
        super(MeEtkinlikKisi.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<MeEtkinlikKisi> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("MeEtkinlikKisi.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
