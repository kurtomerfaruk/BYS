package tr.bel.gaziantep.bysweb.core.exception;


import lombok.Getter;
import tr.bel.gaziantep.bysweb.core.enums.BaseEnum;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.06.2025 08:34
 */
@Getter
public abstract class BysException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -105246263082201263L;

    private BaseEnum errorType;
    private String code;

    public BysException() {
        super();
    }

    public BysException(BaseEnum errorType) {
        super();
        this.errorType = errorType;
    }

    public BysException(BaseEnum errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public BysException(String message, Throwable cause) {
        super(message, cause);
    }

    public BysException(BaseEnum errorType, Throwable cause) {
        super(errorType.getDisplayValue(), cause);
        this.errorType = errorType;
    }

    public BysException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BysException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BysException(String message) {
        super(message);
    }

    public BysException(Throwable cause) {
        super(cause);

    }

    @Override
    public String toString() {

        String string = "";
        if (StringUtil.isBlank(getMessage()) && getErrorType() != null) {
            string = getErrorType().getDisplayValue() + "\n";
        }

        if (StringUtil.isNotBlank(getMessage())) {
            string += getMessage() + "\n";
        }

        if (getCause() != null) {
            string += getCause().toString() + "\n";
        }

        return string;

    }

}