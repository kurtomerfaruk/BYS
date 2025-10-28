package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 26.09.2025 10:50
 */
@Stateless
public class OrtOlcuSablonService  extends AbstractService<OrtOlcuSablon> {

    @Serial
    private static final long serialVersionUID = -5334591679649471931L;

    public OrtOlcuSablonService() {
        super(OrtOlcuSablon.class);
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


    public List<OrtOlcuSablon> findByOlcuId(Integer olcuId) {
        return getEntityManager().createQuery("""
            SELECT DISTINCT s
            FROM OrtOlcuDeger d
            JOIN d.ortOlcuSablonAlan a
            JOIN a.ortOlcuSablon s
            WHERE d.ortOlcu.id = :olcuId
            """, OrtOlcuSablon.class)
                .setParameter("olcuId", olcuId)
                .getResultList();
    }
//
//    public OrtOlcuSablon findSablonByOlcuId(Integer olcuId) {
//        return em.createQuery("""
//            SELECT s FROM OrtOlcuSablon s
//            JOIN OrtOlcuDeger d ON d.ortOlcuSablonAlan.ortOlcuSablon.id = s.id
//            WHERE d.ortOlcu.id = :olcuId
//            """, OrtOlcuSablon.class)
//                .setParameter("olcuId", olcuId)
//                .setMaxResults(1)
//                .getSingleResult();
//    }
}
