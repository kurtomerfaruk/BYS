package tr.bel.gaziantep.bysweb.core.exception;

import jakarta.faces.FacesException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;

import java.util.Iterator;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.06.2025 08:29
 */
@Slf4j
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    private final ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable exception = context.getException();

            try {
                if (exception instanceof BysException) {
                    FacesContext facesContext = FacesContext.getCurrentInstance();
                    FacesMessage message = new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Hata: " + exception.getMessage(),
                            null);
                    facesContext.addMessage(null, message);
                }else if(exception instanceof jakarta.faces.FacesException){
                    FacesUtil.addErrorMessage(exception.getCause().getCause().getMessage());
                }else {
                    // Diğer hatalar için genel mesaj
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    Constants.HATA_OLUSTU, null));
                    log.error("Sistem hatası", exception);
                }
            } finally {
                events.remove();
            }
        }

        getWrapped().handle();
    }
}
