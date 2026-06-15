package tr.bel.gaziantep.bysweb.core.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.sessions.SessionManager;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.12.0
 * @since 15.06.2026 16:16
 */
@Stateless
@Slf4j
public class KillSessionSchedule implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -8339850003364918079L;

    @Inject
    private InitApp initApp;
    @Inject
    private SessionManager sessionManager;

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "18", dayOfWeek = "Mon-Fri", persistent = false)
    public void killSession() {
        try {
            List<SyKullanici> userList = new ArrayList<>(initApp.getSyKullanicis());
            for (SyKullanici user : userList) {
                if (!user.getKullaniciTuru().equals(EnumSyKullaniciTuru.SISTEM_YONETICISI)) {
                    sessionManager.invalidateSession(user.getSessionId());
                    initApp.getSyKullanicis().remove(user);
                }
            }
            log.info("Kill session time : "+LocalDateTime.now());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
