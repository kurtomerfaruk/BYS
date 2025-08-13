package tr.bel.gaziantep.bysweb.core.sessions;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:40
 */
@WebListener
@Slf4j
public class SessionListeners implements HttpSessionListener, java.io.Serializable, ServletContextListener {

    @Serial
    private static final long serialVersionUID = -1914387288968818288L;
    private ServletContext servletContext;

    @Inject
    SessionManager sessionManager;


    private int counter;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        event.getSession().setAttribute("sessionId", event.getSession().getId());
        incrementUserCounter();
        sessionManager.addSession(event.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionId = (String) event.getSession().getAttribute("sessionId");
        List<SyKullanici> kullanicis = (List<SyKullanici>) event.getSession().getAttribute("syKullanicilar");
        if (kullanicis != null) {
            for (SyKullanici kullanici : kullanicis) {
                if (kullanici.getSessionId().equals(sessionId)) {
                    kullanicis.remove(kullanici);
                    break;
                }
            }
        }
        //sessionManager.invalidateSession(sessionId);
        decrementUserCounter();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        servletContext = sce.getServletContext();
        servletContext.setAttribute((Constants.USER_COUNTER), this.counter);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("contextDestroyed");
    }

    synchronized void incrementUserCounter() {
        Integer counterValue = (Integer) servletContext.getAttribute(Constants.USER_COUNTER);
        if (counterValue != null) {
            this.counter = counterValue;
            this.counter++;
            servletContext.setAttribute((Constants.USER_COUNTER), this.counter);
            writeUserCount();
        }
    }

    synchronized void decrementUserCounter() {
        Integer counterValue = (Integer) servletContext.getAttribute(Constants.USER_COUNTER);
        if (counterValue != null) {
            this.counter = counterValue;
            this.counter--;

            if (this.counter < 0) {
                this.counter = 0;
            }

            servletContext.setAttribute((Constants.USER_COUNTER), this.counter);
            writeUserCount();
        }
    }

    private void writeUserCount() {
        log.info("Sistemde toplam " + this.counter + " Aktif kullanici var ");
    }
}
