package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyDuyuruKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 09:07
 */
@Stateless
public class SyDuyuruKullaniciService extends AbstractService<SyDuyuruKullanici> {
    @Serial
    private static final long serialVersionUID = -2266089492388629704L;

    public SyDuyuruKullaniciService() {
        super(SyDuyuruKullanici.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<SyDuyuruKullanici> findByKullanici(SyKullanici syKullanici) {
        return getEntityManager().createNamedQuery("SyDuyuruKullanici.findByKullanici").setParameter("kullanici", syKullanici).getResultList();
    }
}
