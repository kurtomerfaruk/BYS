package tr.bel.gaziantep.bysweb.moduls.engelsizler.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalepKonu;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 15:18
 */
@Stateless
public class EyTalepKonuService extends AbstractService<EyTalepKonu> {

    @Serial
    private static final long serialVersionUID = 5669711144792513958L;

    public EyTalepKonuService() {
        super(EyTalepKonu.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String getSortCol() {
        return "tanim";
    }

    public List<EyTalepKonu> findByModul(EnumModul modul) {
        TypedQuery<EyTalepKonu> query = em.createNamedQuery("EyTalepKonu.findByModul", EyTalepKonu.class);
        query.setParameter("modul", "%" + modul.name() + "%");
        return query.getResultList();
    }
}
