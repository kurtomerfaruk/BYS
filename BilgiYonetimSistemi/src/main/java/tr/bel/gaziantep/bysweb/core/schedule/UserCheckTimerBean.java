package tr.bel.gaziantep.bysweb.core.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 21.08.2025 09:05
 */
@Stateless
@Slf4j
public class UserCheckTimerBean implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -1656271259501526289L;

    @Inject
    private SyKullaniciService syKullaniciService;

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "7", dayOfWeek = "Mon-Fri", persistent = false)
    public void checkUser() {
        try {
            List<SyKullanici> syKullanicis = syKullaniciService.findByKilitli();
            for (SyKullanici syKullanici : syKullanicis) {
                LocalDateTime lastLoginTime = syKullanici.getSonGirisZamani() == null ? syKullanici.getEklemeTarihi() : syKullanici.getSonGirisZamani();
                long days = DateUtil.dateDifferenceDays(lastLoginTime.toLocalDate());
                if (days > 29) {
                    syKullanici.setKilitli(true);
                    syKullaniciService.edit(syKullanici);
                }
            }
            log.info("User checked time : "+LocalDateTime.now());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
