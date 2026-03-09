package tr.bel.gaziantep.bysweb.webservice.api.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import tr.bel.gaziantep.bysweb.core.service.AbstractService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiLog;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 10:21
 */
@Stateless
public class ApiLogService extends AbstractService<ApiLog> {

    @Serial
    private static final long serialVersionUID = 6362877801988684686L;

    public ApiLogService() {
        super(ApiLog.class);
    }

    @PersistenceContext(unitName = Constants.UNIT_NAME)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public void log(String appKey,
                    String method,
                    String path,
                    int status,
                    String ip,
                    long duration) {

        ApiLog log = new ApiLog();
        log.setAppKey(appKey);
        log.setHttpMethod(method);
        log.setEndpoint(path);
        log.setResponseStatus(status);
        log.setClientIp(ip);
        log.setDuration(duration);

       create(log);
    }
}
