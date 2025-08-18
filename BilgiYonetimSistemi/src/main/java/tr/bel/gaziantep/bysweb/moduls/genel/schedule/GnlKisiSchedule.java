package tr.bel.gaziantep.bysweb.moduls.genel.schedule;

import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.util.Date;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 21:49
 */
@Stateless
@Slf4j
public class GnlKisiSchedule implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 1941581628934741847L;

    @EJB
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kpsController;

    @Schedule(minute = "0",second = "0",dayOfMonth = "*",month = "*",year = "*",hour = "2",dayOfWeek = "*",persistent = false)
    public void updateGnlKisi() throws Exception {
        System.out.println("Mernis Kayit Guncelle baslama tarihi :"+new Date());
        listUpdate();
        System.out.println("Mernis Kayit Guncellebitis tarihi:" + new Date());
    }

    private void listUpdate() throws Exception {
        List<GnlKisi> gnlKisiList =gnlKisiService.getByMernisGuncellemeTarihi(1000);

        for (GnlKisi gnlKisi : gnlKisiList) {
            GnlKisi kisi = kpsController.findByTcKimlikNoSchedule(gnlKisi, EnumModul.GENEL);
            if(kisi != null){
                gnlKisiService.edit(kisi);
            }

        }
    }
}
