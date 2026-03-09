package tr.bel.gaziantep.bysweb.webservice.api.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullanici;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 09:56
 */
@Stateless
public class ApiKullaniciService extends AbstractService<ApiKullanici> {

    @Serial
    private static final long serialVersionUID = -322166887349460844L;

    public ApiKullaniciService() {
        super(ApiKullanici.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


    public ApiKullanici findByAppKeyByAppSecret(String appKey, String appSecret) {
        return getEntityManager().createNamedQuery("ApiKullanici.findByAppKeyByAppSecret",ApiKullanici.class)
                .setParameter("appKey", appKey)
                .setParameter("appSecret", appSecret)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }


    public ApiKullanici findByKullaniciIdByIp(Integer id, String ip) {
        return getEntityManager().createNamedQuery("ApiKullanici.findByApiKullaniciIdByIP",ApiKullanici.class)
                .setParameter("id",id)
                .setParameter("ip",ip)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public boolean isIpAllowed(String ip){
        List<ApiKullanici> kisiList= getEntityManager().createNamedQuery("ApiKullanici.findByIp",ApiKullanici.class)
                .setParameter("ip", ip)
                .getResultList();
        return !kisiList.isEmpty();
    }
}
