package tr.bel.gaziantep.bysweb.webservice.api.resource.evimdunyalarabedel;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service.EdbBasvuruService;
import tr.bel.gaziantep.bysweb.webservice.api.dto.PageResponse;
import tr.bel.gaziantep.bysweb.webservice.api.dto.evimdunyalarabedel.EdbBasvuruDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 12.03.2026 23:05
 */
@Path("/evimdunyalarabedel")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EdbBasvuruResource {

    @Inject
    private EdbBasvuruService edbBasvuruService;

    @GET
    @Path("/basvurular")
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

        PageResponse<EdbBasvuruDTO> response = edbBasvuruService.findAll(page, size);

        return Response.ok(response).build();
    }
}
