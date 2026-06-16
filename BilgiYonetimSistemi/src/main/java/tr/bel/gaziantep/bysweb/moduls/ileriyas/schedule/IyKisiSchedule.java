package tr.bel.gaziantep.bysweb.moduls.ileriyas.schedule;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.service.IyKisiService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

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
        LocalDateTime now = LocalDateTime.now();
        SyKullanici ekleyen = new SyKullanici(1);

        int pageSize = 100;
        int first = 0;

        while (true) {

            List<GnlKisi> gnlKisiList = gnlKisiService.findByYasByYasli(60, 0, pageSize);
            if (gnlKisiList.isEmpty()) {
                FacesUtil.addSuccessMessage("Yaşlıar başarıyla aktarıldı");
                break;
            }
            if (first > 2000) {
                service.clearCache();
            }
            for (GnlKisi gnlKisi : gnlKisiList) {
                IyKisi iyKisi = service.findByTcKimlikNo(gnlKisi.getTcKimlikNo());
                if (iyKisi == null) {
                    iyKisi = IyKisi.builder().gnlKisi(gnlKisi).build();
                    iyKisi.setEkleyen(ekleyen);
                    iyKisi.setEklemeTarihi(now);
                }
                iyKisi.getGnlKisi().setYasli(true);

                service.edit(iyKisi);
            }
            first += pageSize;
        }
    }
}
