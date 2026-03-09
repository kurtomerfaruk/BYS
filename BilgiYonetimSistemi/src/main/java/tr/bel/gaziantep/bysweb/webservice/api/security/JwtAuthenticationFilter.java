package tr.bel.gaziantep.bysweb.webservice.api.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullanici;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullaniciRateLimit;
import tr.bel.gaziantep.bysweb.webservice.api.exception.ApiErrorResponse;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiKullaniciRateLimitService;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiKullaniciService;
import tr.bel.gaziantep.bysweb.webservice.api.service.JwtService;
import tr.bel.gaziantep.bysweb.webservice.api.service.RateLimitService;

import java.io.IOException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:16
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;
    @Context
    private HttpServletRequest httpRequest;

    @Inject
    private ApiKullaniciService apiKullaniciService;
    @Inject
    private RateLimitService rateLimitService;
    @Inject
    private ApiKullaniciRateLimitService apiKullaniciRateLimitService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

//        if (resourceInfo.getResourceMethod()
//                .isAnnotationPresent(PermitAll.class)) {
//            return;
//        }

        String path = requestContext.getUriInfo().getPath();

        if (path.contains("auth/token")) {
            return;
        }

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throwException(Response.Status.UNAUTHORIZED, "Authorization header missing");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {

            var claims = JwtService.validateToken(token);

            Integer apiUserId = (Integer) claims.get("apiUserId");
            String clientIp = getClientIp();

            ApiKullanici apiUser = apiKullaniciService.findByKullaniciIdByIp(apiUserId, clientIp);

            if (apiUser == null) {
                abort(requestContext, Response.Status.FORBIDDEN, "IP adresiniz erişim için yetkili değil!!! Sistem yöneticiniz ile görüşün");
                return;
            }

            String method = requestContext.getMethod();

            ApiKullaniciRateLimit ddd = apiKullaniciRateLimitService.find(1);

            ApiKullaniciRateLimit rateLimit = apiKullaniciRateLimitService.findByUserIdByMethodByPath(apiUserId, method, path);

            if (rateLimit != null) {

                boolean allowed = rateLimitService.isAllowed(
                        apiUserId,
                        path,
                        method,
                        rateLimit.getRateLimitPerMinute()
                );

                if (!allowed) {
                    abort(requestContext,
                            Response.Status.TOO_MANY_REQUESTS,
                            "Rate limit exceeded");
                    return;
                }
            }


            requestContext.setProperty("apiUserId", claims.get("apiUserId"));

        } catch (Exception e) {
            if ( e instanceof io.jsonwebtoken.security.SignatureException || e instanceof io.jsonwebtoken.ExpiredJwtException){
                abort(requestContext, Response.Status.UNAUTHORIZED, e.getLocalizedMessage());
                return;
            }
            abort(requestContext, Response.Status.UNAUTHORIZED, "IP adresiniz erişim için yetkili değil!!! Sistem yöneticiniz ile görüşün");
        }
    }

    private String getClientIp() {

        String forwarded = httpRequest.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return httpRequest.getRemoteAddr();
    }

    private void abort(ContainerRequestContext context, Response.Status status, String message) {
        String path = context.getUriInfo().getPath();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                status.getStatusCode(),
                status.getReasonPhrase(),
                message,
                path
        );

        context.abortWith(
                Response.status(status)
                        .entity(errorResponse)
                        .type(MediaType.APPLICATION_JSON)
                        .build()
        );
    }

    private void throwException(Response.Status status, String message) {
        throw new jakarta.ws.rs.WebApplicationException(
                message,
                Response.status(status).build()
        );
    }
}
