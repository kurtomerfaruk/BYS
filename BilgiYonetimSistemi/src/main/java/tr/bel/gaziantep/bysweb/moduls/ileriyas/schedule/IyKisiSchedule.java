package tr.bel.gaziantep.bysweb.moduls.ileriyas.schedule;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.service.IyKisiService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.10.0
 * @since 11.06.2026 09:31
 */
@Stateless
@Slf4j
public class IyKisiSchedule implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8147384234821345080L;

    @EJB
    private GnlKisiService gnlKisiService;
    @EJB
    private IyKisiService service;

    @Schedule(minute = "45", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "3", dayOfWeek = "Mon-Fri", persistent = false)
    public void addIyKisi() throws Exception {
        try {
            log.info("IyKisi kontrol baslama tarihi :" + LocalDateTime.now());
            addList();
            log.info("IyKisi kontrol bitis tarihi:" + LocalDateTime.now());
        } catch (Exception e) {
            log.error("Engelsizler Talepler guncellenirken hata olustu : " + e.getMessage());
        }
    }

    private void addList() {
        Set<Integer> mevcutKisiIdleri = service.findAll().stream()
                .map(k -> k.getGnlKisi().getId())
                .collect(Collectors.toSet());

        LocalDateTime now = LocalDateTime.now();
        SyKullanici ekleyen = new SyKullanici(1);

        int pageSize = 100;
        int first = 0;

        while (true) {

            List<GnlKisi> kisiler = gnlKisiService.findByYasByYasli(60, first, pageSize);

            if (kisiler.isEmpty()) {
                break;
            }

            kisiler.stream()
                    .filter(k -> !mevcutKisiIdleri.contains(k.getId()))
                    .map(k -> {
                        k.setYasli(true);
                        IyKisi iyKisi = IyKisi.builder()
                                .gnlKisi(k)
                                .build();

                        iyKisi.setEkleyen(ekleyen);
                        iyKisi.setEklemeTarihi(now);

                        return iyKisi;
                    })
                    .forEach(service::create);

            first += pageSize;
        }
    }
}
