package tr.bel.gaziantep.bysweb.moduls.aktifyasam.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisiGrup;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.12.2025 11:06
 */
@Stateless
public class AyKisiGrupService extends AbstractService<AyKisiGrup> {

    @Serial
    private static final long serialVersionUID = 8275562440323450907L;

    public AyKisiGrupService() {
        super(AyKisiGrup.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<AyKisi> findByGroup(EnumAyGrup group) {
        return getEntityManager().createNamedQuery("AyKisiGrup.findByGroup").setParameter("grup", group).getResultList();
    }
}
