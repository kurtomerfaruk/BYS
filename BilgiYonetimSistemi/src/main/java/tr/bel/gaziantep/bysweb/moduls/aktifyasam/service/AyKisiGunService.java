package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisiGun;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.12.2025 16:07
 */
@Stateless
public class AyKisiGunService extends AbstractService<AyKisiGun> {

    @Serial
    private static final long serialVersionUID = -6685846397649982643L;

    public AyKisiGunService() {
        super(AyKisiGun.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyKisi> findByGun(EnumGnlGun day) {
        return getEntityManager().createNamedQuery("AyKisiGun.findByGun").setParameter("gun", day).getResultList();
    }
}
