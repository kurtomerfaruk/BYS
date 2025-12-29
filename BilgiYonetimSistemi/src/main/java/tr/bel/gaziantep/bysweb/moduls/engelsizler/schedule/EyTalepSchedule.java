package tr.bel.gaziantep.bysweb.moduls.engelsizler.schedule;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyTalep;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.12.2025 08:58
 */
@Stateless
@Slf4j
public class EyTalepSchedule implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4729683957113731985L;

    @EJB
    private EyTalepService eyTalepService;

    @Schedule(minute = "30", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "4", dayOfWeek = "Mon-Fri", persistent = false)
    public void updateEyTalep() throws Exception {
        try {
            log.info("EyTalep kontrol baslama tarihi :" + LocalDateTime.now());
            listUpdate();
            log.info("EyTalep kontrol bitis tarihi:" + LocalDateTime.now());
        } catch (Exception e) {
            log.error("Engelsizler Talepler guncellenirken hata olustu : " + e.getMessage());
        }
    }

    private void listUpdate() {
        List<EyTalep> list = eyTalepService.findByDurum(EnumGnlTalepDurumu.BEKLIYOR);
        for (EyTalep talep : list) {
            talep.setDurum(EnumGnlTalepDurumu.TAMAMLANMADI);
            talep.setDurumAciklama("Talep kişinin vefat etmesi sebebiyle tamamlanamadı. Bu işlem sistem tarafından otomatik olarak güncellendi.");
            talep.setGuncelleyen(new SyKullanici(1));
            eyTalepService.edit(talep);
        }
    }
}
