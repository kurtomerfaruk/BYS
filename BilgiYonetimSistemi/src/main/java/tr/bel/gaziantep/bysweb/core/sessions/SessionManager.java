package tr.bel.gaziantep.bysweb.core.sessions;

import jakarta.ejb.Singleton;
import jakarta.servlet.http.HttpSession;

import java.io.Serial;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.06.2025 11:41
 */
@Singleton
public class SessionManager implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -6920134077125191255L;

    private Map<String, HttpSession> activeSessions = new ConcurrentHashMap<>();

    public void addSession(HttpSession session) {
        activeSessions.put(session.getId(), session);
    }

    public void invalidateSession(String sessionId) {
        HttpSession session = activeSessions.get(sessionId);
        if (session != null) {
            session.invalidate();
            activeSessions.remove(sessionId);
        }
    }
}

