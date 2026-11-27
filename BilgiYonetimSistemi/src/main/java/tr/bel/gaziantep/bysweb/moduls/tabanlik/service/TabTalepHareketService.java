package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalepHareket;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:44
 */
@Stateless
public class TabTalepHareketService extends AbstractService<TabTalepHareket> {

    @Serial
    private static final long serialVersionUID = -539703882612407990L;

    public TabTalepHareketService() {
        super(TabTalepHareket.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void add(TabTalep tabTalep, EnumTabTalepDurumu eskiDurum, EnumTabTalepDurumu yeniDurum,
                    String islem, String aciklama) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return;
        }
        SyKullanici kullanici = Util.getSyKullanici();
        TabTalepHareket hareket = TabTalepHareket.builder()
                .tabTalep(tabTalep)
                .eskiDurum(eskiDurum)
                .yeniDurum(yeniDurum)
                .islem(islem)
                .islemTarihi(LocalDateTime.now())
                .islemiYapan(kullanici)
                .aciklama(aciklama)
                .build();
        create(hareket);
    }

    public List<TabTalepHareket> findByTalep(TabTalep tabTalep) {
        if (tabTalep == null || tabTalep.getId() == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT h FROM TabTalepHareket h WHERE h.aktif=true AND h.tabTalep = :talep " +
                                "ORDER BY h.islemTarihi DESC, h.id DESC",
                        TabTalepHareket.class)
                .setParameter("talep", tabTalep)
                .getResultList();
    }
}
