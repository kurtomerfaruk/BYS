package tr.bel.gaziantep.bysweb.core.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.omnifaces.util.Servlets;

import java.io.IOException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:36
 */
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = {"/*"})
public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String loginURI = req.getContextPath() + "/giris";
        String flushURI = req.getContextPath() + "/flush";
        String captchaURI = req.getContextPath() + "/captcha";
        String surveyURI = req.getContextPath() + "/anket";

        boolean loggedIn = session != null && session.getAttribute("syKullanici") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);
        boolean resourceRequest = Servlets.isFacesResourceRequest(req);
        boolean flushRequest = req.getRequestURI().equals(flushURI);
        boolean captchaRequest = req.getRequestURI().equals(captchaURI);
        boolean surveyRequest = req.getRequestURI().equals(surveyURI);

        if (flushRequest) {
            chain.doFilter(req, response);
            return;
        }

        if(surveyRequest) {
            chain.doFilter(req, response);
            return;
        }

        if(captchaRequest) {
            chain.doFilter(req, response);
            return;
        }

        if (loggedIn || loginRequest || resourceRequest) {
            if (loggedIn && loginRequest) {
                res.sendRedirect(req.getContextPath() + "/index");
            } else {
                chain.doFilter(req, response);
            }
        } else {
            res.sendRedirect(loginURI);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Nothing to do here!
    }

    @Override
    public void destroy() {
        // Nothing to do here!
    }

}
