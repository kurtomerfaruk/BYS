package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabUretim;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Stateless
public class TabUretimService extends AbstractService<TabUretim> {

    @Serial
    private static final long serialVersionUID = -1762049385019382751L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public TabUretimService() {
        super(TabUretim.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabUretim> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT u FROM TabUretim u WHERE u.aktif=true AND u.tabTalep = :talep " +
                                "ORDER BY u.baslangicTarihi DESC, u.id DESC",
                        TabUretim.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public TabUretim findLatestByTalep(TabTalep tabTalep) {
        return findByTalep(tabTalep).stream().findFirst().orElse(null);
    }
}
