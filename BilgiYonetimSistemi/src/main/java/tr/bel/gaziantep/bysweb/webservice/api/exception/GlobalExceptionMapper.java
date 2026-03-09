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
 * @since 18.02.2026 14:35
 */
@Provider
@Priority(2)
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {

        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String error = "Internal Server Error";
        String message = "Unexpected error occurred";

        if (exception instanceof WebApplicationException webEx) {

            status = webEx.getResponse().getStatus();
            error = Response.Status.fromStatusCode(status).getReasonPhrase();
            message = webEx.getMessage();
        }

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
