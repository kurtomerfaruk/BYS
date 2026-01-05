package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKolon;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 13:50
 */
@Stateless
public class SyKolonService extends AbstractService<SyKolon> {
    @Serial
    private static final long serialVersionUID = -6244747844722152094L;

    public SyKolonService() {
        super(SyKolon.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SyKolon> findByTabloAdi(String tabloAdi) {
        return getEntityManager().createNamedQuery("SyKolon.findByTabloAdi").setParameter("tabloAdi", tabloAdi).getResultList();
    }
}
