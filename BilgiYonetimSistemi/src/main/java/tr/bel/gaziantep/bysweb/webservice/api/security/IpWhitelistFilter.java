package tr.bel.gaziantep.bysweb.webservice.api.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiKullaniciService;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 16:48
 */
@Provider
@Priority(Priorities.AUTHENTICATION - 1)
public class IpWhitelistFilter implements ContainerRequestFilter {

    @Inject
    private ApiKullaniciService apiKullaniciService;

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext context) {

        String clientIp = getClientIp();

        if (clientIp.equals("0:0:0:0:0:0:0:1")) {
            return;
        }

        // Token endpoint ise body'den appKey alabiliriz
        String path = context.getUriInfo().getPath();

        if (path.startsWith("auth/token")) {

            // IP kontrolü yap
            if (!apiKullaniciService.isIpAllowed(clientIp)) {
                throw new WebApplicationException(
                        "IP adresiniz erişim için yetkili değil!!! Sistem yöneticiniz ile görüşün",
                        Response.Status.FORBIDDEN
                );
            }

            return;
        }

        // Diğer endpointlerde de kontrol et
        if (!apiKullaniciService.isIpAllowed(clientIp)) {
            throw new WebApplicationException(
                    "IP adresiniz erişim için yetkili değil!!! Sistem yöneticiniz ile görüşün",
                    Response.Status.FORBIDDEN
            );
        }
    }

    private String getClientIp() {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0];
        }

        return request.getRemoteAddr();
    }
}
