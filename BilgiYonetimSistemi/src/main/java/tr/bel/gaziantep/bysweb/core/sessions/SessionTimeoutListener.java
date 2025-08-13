package tr.bel.gaziantep.bysweb.core.sessions;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:41
 */
@WebListener
public class SessionTimeoutListener implements PhaseListener {

    @Serial
    private static final long serialVersionUID = 653758087999069152L;

    private String getLoginPath() {
        return "/giris";
    }

    @Override
    public void beforePhase(final PhaseEvent event) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!facesContext.getPartialViewContext().isAjaxRequest() || facesContext.getRenderResponse()) { // not ajax or too late
            return;
        }

        final HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (request.getDispatcherType() == DispatcherType.FORWARD && getLoginPath().equals(request.getServletPath())) { // isLoginRedirection()
            final String redirect = facesContext.getExternalContext().getRequestContextPath() + request.getServletPath();
            try {
                facesContext.getExternalContext().redirect(redirect);
            } catch (final IOException e) {
                // here use you preferred logging framework to log this error
            }
        }
        System.out.println("timeout");
    }

    @Override
    public void afterPhase(final PhaseEvent event) {
        // no-op
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
