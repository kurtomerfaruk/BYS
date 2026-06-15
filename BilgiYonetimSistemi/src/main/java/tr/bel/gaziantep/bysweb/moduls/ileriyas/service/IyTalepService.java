package tr.bel.gaziantep.bysweb.moduls.ileriyas.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyTalep;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.11.0
 * @since 15.06.2026 08:28
 */
@Stateless
public class IyTalepService extends AbstractService<IyTalep> {

    @Serial
    private static final long serialVersionUID = 7408610609223671348L;

    public IyTalepService() {
        super(IyTalep.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<IyTalep> findByDurum(EnumGnlTalepDurumu durum) {
        return getEntityManager().createNamedQuery("IyTalep.findByDurum",IyTalep.class).setParameter("durum", durum).getResultList();
    }
}
