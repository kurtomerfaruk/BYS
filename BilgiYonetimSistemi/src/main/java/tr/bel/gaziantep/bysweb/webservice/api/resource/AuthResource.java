package tr.bel.gaziantep.bysweb.webservice.api.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tr.bel.gaziantep.bysweb.webservice.api.dto.request.AuthRequest;
import tr.bel.gaziantep.bysweb.webservice.api.dto.response.AuthResponse;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullanici;
import tr.bel.gaziantep.bysweb.webservice.api.service.ApiKullaniciService;
import tr.bel.gaziantep.bysweb.webservice.api.service.JwtService;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 14:07
 */
@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private ApiKullaniciService apiKullaniciService;

    @POST
    @Path("/token")
    public Response token(AuthRequest request) {

        ApiKullanici apiUser = apiKullaniciService.findByAppKeyByAppSecret(request.getAppKey(), request.getAppSecret());
        if (apiUser == null) {
            throw new WebApplicationException(
                    "Kullanıcı adı veya parolanız yanlış!!! Sistem yöneticiniz ile görüşün",
                    Response.Status.UNAUTHORIZED
            );
        }

        String token = JwtService.generateToken(apiUser.getId(), apiUser.getAppKey());

        return Response.ok(new AuthResponse(token, 3600)).build();
    }
}
