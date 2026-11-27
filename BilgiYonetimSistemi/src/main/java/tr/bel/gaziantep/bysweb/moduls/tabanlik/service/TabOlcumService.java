package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabAyakTarafi;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabOlcum;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 14:36
 */
@Stateless
public class TabOlcumService extends AbstractService<TabOlcum> {

    @Serial
    private static final long serialVersionUID = -1467183631795527840L;

    public TabOlcumService() {
        super(TabOlcum.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabOlcum> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT o FROM TabOlcum o WHERE o.aktif=true AND o.tabTalep = :talep " +
                                "ORDER BY o.olcumTarihi DESC, o.id DESC",
                        TabOlcum.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }

    public boolean existsByTalep(TabTalep tabTalep) {
        return !findByTalep(tabTalep).isEmpty();
    }

    public boolean hasBothSides(TabOlcum olcum) {
        if (olcum == null || olcum.getDetayList() == null) {
            return false;
        }
        boolean sag = olcum.getDetayList().stream().anyMatch(d -> d.getAyakTarafi() == EnumTabAyakTarafi.SAG);
        boolean sol = olcum.getDetayList().stream().anyMatch(d -> d.getAyakTarafi() == EnumTabAyakTarafi.SOL);
        return sag && sol;
    }
}

