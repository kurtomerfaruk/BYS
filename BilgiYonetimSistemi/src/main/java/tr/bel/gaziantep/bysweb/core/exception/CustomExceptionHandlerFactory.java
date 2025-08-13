package tr.bel.gaziantep.bysweb.core.exception;

import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerFactory;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.06.2025 08:29
 */
public class CustomExceptionHandlerFactory extends ExceptionHandlerFactory {
    private final ExceptionHandlerFactory parent;

    public CustomExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new CustomExceptionHandler(parent.getExceptionHandler());
    }
}
