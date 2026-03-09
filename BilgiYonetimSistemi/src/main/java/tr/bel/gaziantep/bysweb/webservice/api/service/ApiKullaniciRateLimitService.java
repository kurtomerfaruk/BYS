package tr.bel.gaziantep.bysweb.webservice.api.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullaniciRateLimit;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 10:42
 */
@Stateless
public class ApiKullaniciRateLimitService extends AbstractService<ApiKullaniciRateLimit> {

    @Serial
    private static final long serialVersionUID = -3980685218434217175L;

    public ApiKullaniciRateLimitService() {
        super(ApiKullaniciRateLimit.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }



    public ApiKullaniciRateLimit findByUserIdByMethodByPath(Integer apiUserId, String method, String path) {
        return getEntityManager().createNamedQuery("ApiKullaniciRateLimit.findByUserIdByMethodByPath",ApiKullaniciRateLimit.class)
                .setParameter("apiUserId",apiUserId)
                .setParameter("method",method)
                .setParameter("path","/"+path)
                .getResultStream().findFirst()
                .orElse(null);
    }
}
