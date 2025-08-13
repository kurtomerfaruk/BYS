package tr.bel.gaziantep.bysweb.core.exception;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.06.2025 08:26
 */
public class BysExceptionHandlerFactory extends ExceptionHandlerFactory {

    public BysExceptionHandlerFactory(ExceptionHandlerFactory wrapped) {
        super(wrapped);
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new BysExceptionHandler(getWrapped().getExceptionHandler());
    }
}
