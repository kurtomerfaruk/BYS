package tr.bel.gaziantep.bysweb.core.exception;

import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 1.10.2025 09:02
 */
public class BysBusinessException extends BysException{

    public BysBusinessException() {
        super();
    }

    public BysBusinessException(BaseEnum errorType) {
        super(errorType, errorType.getDisplayValue());
    }

    public BysBusinessException(BaseEnum errorType, String message) {
        super(errorType, message);
    }

    public BysBusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public BysBusinessException(BaseEnum errorType, Throwable throwable) {
        super(errorType, throwable);
    }

    public BysBusinessException(String message, String code) {
        super(message, code);
    }

    public BysBusinessException(String message, String code, Throwable cause) {
        super(message, code, cause);
    }

    public BysBusinessException(String message) {
        super(message);
    }

    public BysBusinessException(Throwable throwable) {
        super(throwable);
    }
}
