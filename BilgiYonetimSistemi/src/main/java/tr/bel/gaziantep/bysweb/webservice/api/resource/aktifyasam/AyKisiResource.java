package tr.bel.gaziantep.bysweb.webservice.api.resource.aktifyasam;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyKisiService;
import tr.bel.gaziantep.bysweb.webservice.api.dto.PageResponse;
import tr.bel.gaziantep.bysweb.webservice.api.dto.aktifyasam.AyKisiDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 9.03.2026 20:14
 */
@Path("/aktifyasam")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AyKisiResource {

    @Inject
    private AyKisiService ayKisiService;

    @GET
    @Path("/kisiler")
    public Response getPersons(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("20") int size) {

        if (size > 20) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Size parametresi 20'den büyük olamaz. En fazla 20 kayıt çağırabilirsiniz...");

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(error)
                    .build();
        }

        PageResponse<AyKisiDTO> response = ayKisiService.findAll(page, size);

        return Response.ok(response).build();
    }
}
