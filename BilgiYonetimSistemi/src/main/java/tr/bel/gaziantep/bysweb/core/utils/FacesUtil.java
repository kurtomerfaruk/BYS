package tr.bel.gaziantep.bysweb.core.utils;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItem;
import jakarta.faces.context.FacesContext;

import java.util.ResourceBundle;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 11:59
 */
public class FacesUtil {

    public static String message(String message) {
        return getBundle().getString(message);
    }

    public static ResourceBundle getBundle() {
        FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle bundle = ResourceBundle.getBundle("/messages", context.getViewRoot().getLocale());
        return bundle;
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static Throwable getRootCause(Throwable cause) {
        if (cause != null) {
            Throwable source = cause.getCause();
            if (source != null) {
                return getRootCause(source);
            } else {
                return cause;
            }
        }
        return null;
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();

    }

    public static void errorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message(msg), message(msg));
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    public static boolean isValidationFailed() {
        return FacesContext.getCurrentInstance().isValidationFailed();
    }


    public static void successMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, message(msg), message(msg));
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    public static void warningMessage(String mesaj) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, message(mesaj), message(mesaj));
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    public static void addExclamationMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        FacesContext.getCurrentInstance().validationFailed();
    }

    public static boolean isDummySelectItem(UIComponent component, String value) {
        for (UIComponent children : component.getChildren()) {
            if (children instanceof UISelectItem) {
                UISelectItem item = (UISelectItem) children;
                if (item.getItemValue() == null && item.getItemLabel().equals(value)) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}
