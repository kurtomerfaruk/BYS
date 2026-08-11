package tr.bel.gaziantep.bysweb.core.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;

import java.io.Serial;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.08.2025 21:21
 */
@Stateless
@Slf4j
public class PasswordCheckTimerBean implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4998119376016445195L;

    @Inject
    private SyKullaniciService syKullaniciService;

    @Schedule(minute = "*/10", hour = "*", persistent = false)
    public void checkPasswordChange() {
        try {
            List<SyKullanici> syKullanicis = syKullaniciService.findByParolaDegistirilsin();
            for (SyKullanici syKullanici : syKullanicis) {
                LocalDateTime lastLoginTime = syKullanici.getSonGirisZamani();
                if (lastLoginTime != null) {
                    long days = Duration.between(lastLoginTime, LocalDateTime.now()).toDays();
                    if (days >= 1) {
                        syKullanici.setKilitli(true);
                        syKullaniciService.edit(syKullanici);
                    }
                }
            }
            log.info("Password change control done : " + LocalDateTime.now());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
