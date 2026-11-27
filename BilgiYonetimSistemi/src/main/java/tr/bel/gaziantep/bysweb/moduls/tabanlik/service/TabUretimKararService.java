package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabUretimKarar;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 14:59
 */
@Stateless
public class TabUretimKararService extends AbstractService<TabUretimKarar> {

    @Serial
    private static final long serialVersionUID = 6891696527741393156L;

    public TabUretimKararService() {
        super(TabUretimKarar.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabUretimKarar> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT k FROM TabUretimKarar k WHERE k.aktif=true AND k.tabTalep = :talep " +
                                "ORDER BY k.kararTarihi DESC, k.id DESC",
                        TabUretimKarar.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }
}
