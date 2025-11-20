package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 3.07.2025 09:03
 */
@Stateless
public class AyGirisCikisService extends AbstractService<AyGirisCikis> {

    @Serial
    private static final long serialVersionUID = 4314733345341349926L;

    public AyGirisCikisService() {
        super(AyGirisCikis.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyKisi> getEntrants() {
        return getEntityManager().createNamedQuery("AyGirisCikis.findGirisYapanlar").getResultList();
    }

    public List<AyGirisCikis> findByGnlKisi(GnlKisi kisi) {
        return getEntityManager().createNamedQuery("AyGirisCikis.findByGnlKisi").setParameter("gnlKisi",kisi).getResultList();
    }
}
