package tr.bel.gaziantep.bysweb.moduls.ortezprotez.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcu;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuDeger;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtOlcuSablon;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 2.10.2025 14:13
 */
@Stateless
public class OrtOlcuDegerService extends AbstractService<OrtOlcuDeger> {

    @Serial
    private static final long serialVersionUID = -3743023152266372048L;

    public OrtOlcuDegerService() {
        super(OrtOlcuDeger.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<OrtOlcuDeger> findByOrtOlcu(OrtOlcu ortOlcu) {
        return getEntityManager().createNamedQuery("OrtOlcuDeger.findByOrtOlcu").setParameter("ortOlcu", ortOlcu).getResultList();
    }

    public List<OrtOlcuDeger> findByOrtOlcuByOrtOlcuSablon(OrtOlcu ortOlcu, OrtOlcuSablon ortOlcuSablon) {
        if(ortOlcu.getId()==null){
            return new ArrayList<>();
        }
        return getEntityManager().createNamedQuery("OrtOlcuDeger.findByOrtOlcuByOrtOlcuSablon")
                .setParameter("ortOlcu", ortOlcu)
                .setParameter("ortOlcuSablon", ortOlcuSablon)
                .getResultList();
    }

//    public List<OrtOlcuDegerDTO> findByOlcuAndSablon(Integer olcuId, Integer sablonId) {
//        return em.createQuery("""
//            SELECT new tr.bel.gaziantep.bysweb.moduls.ortezprotez.dto.OrtOlcuDegerDTO(
//                a.tanim,
//                a.tur,
//                d.deger
//            )
//            FROM OrtOlcuDeger d
//            JOIN d.ortOlcuSablonAlan a
//            WHERE d.ortOlcu.id = :olcuId
//              AND a.ortOlcuSablon.id = :sablonId
//            ORDER BY a.id
//            """, OrtOlcuDegerDTO.class)
//                .setParameter("olcuId", olcuId)
//                .setParameter("sablonId", sablonId)
//                .getResultList();
//    }
}
