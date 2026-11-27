package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabOdeme;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;

import java.io.Serial;
import java.util.List;

/**
 * @author OmerFarukKurt
 * @version 1.0.0
 */
@Stateless
public class TabOdemeService extends AbstractService<TabOdeme> {

    @Serial
    private static final long serialVersionUID = 5039182746193875012L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public TabOdemeService() {
        super(TabOdeme.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabOdeme> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT o FROM TabOdeme o WHERE o.aktif=true AND o.tabTalep = :talep " +
                                "ORDER BY o.id DESC",
                        TabOdeme.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public TabOdeme findLatestByTalep(TabTalep tabTalep) {
        return findByTalep(tabTalep).stream().findFirst().orElse(null);
    }
}
