package tr.bel.gaziantep.bysweb.core.utils;

import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 13.06.2025 11:58
 */
public class Util {

    public static HttpSession getSession() {
        return (HttpSession) getExternalContext().getSession(false);
    }

    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }

    public static SyKullanici getSyKullanici() {
        try {
            return (SyKullanici) getExternalContext().getSessionMap().get("syKullanici");
        } catch (Exception e) {
            return null;
        }
    }


    public static ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getContext();
    }

    public static ExternalContext getExternalContext() {
        return getFacesContext().getExternalContext();
    }

    public static FacesContext getFacesContext() {
        return FacesContext.getCurrentInstance();
    }

    public static String getParameter(String value) {
        return getExternalContext().getRequestParameterMap().get(value);
    }

    public static String getRealPath(String value) {
        return getExternalContext().getRealPath(value);
    }
}
