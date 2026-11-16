package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.03.2026 12:52
 */
@Stateless
public class SyGenelAyarService extends AbstractService<SyGenelAyar> {

    @Serial
    private static final long serialVersionUID = 6721662596429970765L;

    public SyGenelAyarService() {
        super(SyGenelAyar.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SyGenelAyar findByTanim(String tanim) {
            return (SyGenelAyar) getEntityManager()
                    .createNamedQuery("SyGenelAyar.findByTanim", SyGenelAyar.class)
                    .setParameter("tanim",tanim)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
    }
}
