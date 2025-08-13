package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciKolon;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 13:50
 */
@Stateless
public class SyKullaniciKolonService extends AbstractService<SyKullaniciKolon> {

    @Serial
    private static final long serialVersionUID = 5488996345082313871L;

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public SyKullaniciKolonService() {
        super(SyKullaniciKolon.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SyKullaniciKolon> findByKullaniciByTablo(SyKullanici kullanici, String tableName) {
        return getEntityManager().createNamedQuery("SyKullaniciKolon.findByKullaniciByTablo")
                .setParameter("syKullanici", kullanici)
                .setParameter("tablo", tableName)
                .getResultList();
    }
}
