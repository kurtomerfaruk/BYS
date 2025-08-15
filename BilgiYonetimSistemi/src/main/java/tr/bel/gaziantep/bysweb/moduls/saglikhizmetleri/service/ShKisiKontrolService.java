package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKontrol;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 08:37
 */
@Stateless
public class ShKisiKontrolService extends AbstractService<ShKisiKontrol> {

    @Serial
    private static final long serialVersionUID = -7019964302645188129L;

    public ShKisiKontrolService() {
        super(ShKisiKontrol.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<ShKisiKontrol> findByGnlKisi(GnlKisi kisi) {
        return  getEntityManager().createNamedQuery("ShKisiKontrol.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
