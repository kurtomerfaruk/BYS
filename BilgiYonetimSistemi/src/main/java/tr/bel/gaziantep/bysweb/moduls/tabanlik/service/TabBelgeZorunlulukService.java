package tr.bel.gaziantep.bysweb.moduls.tabanlik.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabBelgeTuru;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabUcretTuru;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabBelgeZorunluluk;

import java.io.Serial;
import java.util.List;

/**
 * Ücretli/ücretsiz süreçler için zorunlu belge kurallarını veritabanından okur.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Stateless
public class TabBelgeZorunlulukService extends AbstractService<TabBelgeZorunluluk> {

    @Serial
    private static final long serialVersionUID = -7362049182736450191L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public TabBelgeZorunlulukService() {
        super(TabBelgeZorunluluk.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<TabBelgeZorunluluk> findByIslem(EnumTabUcretTuru ucretTuru, String islem) {
        if (ucretTuru == null || islem == null) {
            return List.of();
        }
        return getEntityManager()
                .createQuery("SELECT k FROM TabBelgeZorunluluk k WHERE k.aktif=true AND k.zorunlu=true " +
                                "AND k.ucretTuru = :tur AND k.islem = :islem",
                        TabBelgeZorunluluk.class)
                .setParameter("tur", ucretTuru)
                .setParameter("islem", islem)
                .getResultList();
    }

    public List<EnumTabBelgeTuru> getRequiredTurler(EnumTabUcretTuru ucretTuru, String islem) {
        return findByIslem(ucretTuru, islem).stream()
                .map(TabBelgeZorunluluk::getBelgeTuru)
                .toList();
    }
}
