package tr.bel.gaziantep.bysweb.moduls.moralevi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeGirisCikis;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.10.2025 11:07
 */
@Stateless
public class MeGirisCikisService extends AbstractService<MeGirisCikis> {

    @Serial
    private static final long serialVersionUID = 4931990250476831383L;

    public MeGirisCikisService() {
        super(MeGirisCikis.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<MeGirisCikis> findByEntrance() {
        return getEntityManager().createNamedQuery("MeGirisCikis.findByEntrance").getResultList();
    }
}
