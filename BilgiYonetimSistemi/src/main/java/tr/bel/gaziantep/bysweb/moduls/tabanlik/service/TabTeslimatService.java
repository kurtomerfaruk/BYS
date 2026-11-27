package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTeslimat;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Stateless
public class TabTeslimatService extends AbstractService<TabTeslimat> {

    @Serial
    private static final long serialVersionUID = -1762039485019382751L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public TabTeslimatService() {
        super(TabTeslimat.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabTeslimat> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT t FROM TabTeslimat t WHERE t.aktif=true AND t.tabTalep = :talep " +
                                "ORDER BY t.teslimatTarihi DESC, t.id DESC",
                        TabTeslimat.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public boolean existsByTalep(TabTalep tabTalep) {
        return !findByTalep(tabTalep).isEmpty();
    }
}
