package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciGiris;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 14:43
 */
@Stateless
public class SyKullaniciGirisService extends AbstractService<SyKullaniciGiris> {

    @Serial
    private static final long serialVersionUID = -6128337807150066929L;
    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    public SyKullaniciGirisService() {
        super(SyKullaniciGiris.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
