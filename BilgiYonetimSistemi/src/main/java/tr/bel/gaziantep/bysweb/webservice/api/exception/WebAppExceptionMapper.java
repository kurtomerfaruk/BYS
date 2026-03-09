package tr.bel.gaziantep.bysweb.webservice.api.exception;

import jakarta.annotation.Priority;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.20.0
 * @since 18.02.2026 15:25
 */
@Provider
@Priority(1)
public class WebAppExceptionMapper implements ExceptionMapper<WebApplicationException> {
    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(WebApplicationException exception) {

        int status = exception.getResponse().getStatus();
        String error = Response.Status.fromStatusCode(status).getReasonPhrase();
        String message = exception.getMessage();

        ApiErrorResponse apiError = new ApiErrorResponse(
                status,
                error,
                message,
                uriInfo.getPath()
        );

        return Response.status(status)
                .entity(apiError)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
