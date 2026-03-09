package tr.bel.gaziantep.bysweb.webservice.api.security;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullanici;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiLog;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiKullaniciService;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiLogService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:33
 */
@Provider
@Priority(Priorities.USER)
public class ApiLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    private ApiLogService apiLogService;
    @Inject
    private ApiKullaniciService apiKullaniciService;

    @Context
    private HttpServletRequest httpRequest;

    private static final String START_TIME = "startTime";
    private static final String REQUEST_BODY = "requestBody";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        requestContext.setProperty(START_TIME, System.currentTimeMillis());

        // Request body capture
        if (requestContext.hasEntity()) {

            InputStream in = requestContext.getEntityStream();
            String body = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            requestContext.setProperty(REQUEST_BODY, body);

            requestContext.setEntityStream(
                    new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8))
            );
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {

        try {

            long start = (long) requestContext.getProperty(START_TIME);
            long duration = System.currentTimeMillis() - start;

            Integer apiUserId =
                    (Integer) requestContext.getProperty("apiUserId");

            String appKey = null;
            ApiKullanici user = null;
            if (apiUserId != null) {
                user = apiKullaniciService.find(apiUserId);
                if (user != null) {
                    appKey = user.getAppKey();
                }
            }

            ApiLog log = new ApiLog();
            log.setAppKey(appKey);
            log.setEndpoint(requestContext.getUriInfo().getPath());
            log.setHttpMethod(requestContext.getMethod());
            log.setRequestBody((String) requestContext.getProperty(REQUEST_BODY));
            log.setRequestUri(requestContext.getUriInfo().getRequestUri().getPath());
            log.setResponseStatus(responseContext.getStatus());
            log.setClientIp(getClientIp());
            log.setDuration(duration);
            if (user != null) {
                log.setEkleyen(user);
            }
            log.setEklemeTarihi(LocalDateTime.now());


            apiLogService.create(log);

        } catch (Exception ignored) {
            System.out.println(ignored.getLocalizedMessage());
        }
    }

    private String getClientIp() {

        String forwarded = httpRequest.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return httpRequest.getRemoteAddr();
    }
}