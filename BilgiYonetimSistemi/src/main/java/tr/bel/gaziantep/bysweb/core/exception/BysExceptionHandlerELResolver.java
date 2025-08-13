package tr.bel.gaziantep.bysweb.core.exception;

import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.lifecycle.ClientWindow;
import org.primefaces.application.exceptionhandler.ExceptionInfo;
import org.primefaces.util.LangUtils;

import java.util.Map;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.06.2025 08:25
 */
public class BysExceptionHandlerELResolver extends ELResolver {

    public static final String EL_NAME = "bysExceptionHandler";

    @Override
    public Object getValue(ELContext elContext, Object base, Object property) {

        if (EL_NAME.equals(property)) {
            elContext.setPropertyResolved(true);

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            ExceptionInfo info = (ExceptionInfo) context.getAttributes().get(ExceptionInfo.ATTRIBUTE_NAME);

            if (info == null && externalContext.getSession(false) != null) {
                ClientWindow clientWindow = externalContext.getClientWindow();
                if (clientWindow != null && LangUtils.isNotBlank(clientWindow.getId())) {
                    Map<String, ExceptionInfo> windowsMap = (Map<String, ExceptionInfo>)
                            externalContext.getSessionMap().get(ExceptionInfo.ATTRIBUTE_NAME + "_map");
                    if (windowsMap != null) {
                        info = windowsMap.get(clientWindow.getId());
                    }
                }
                if (info == null) {
                    info = (ExceptionInfo) externalContext.getSessionMap().get(ExceptionInfo.ATTRIBUTE_NAME);
                }
            }

            return info;
        }

        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        // readonly
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }
}