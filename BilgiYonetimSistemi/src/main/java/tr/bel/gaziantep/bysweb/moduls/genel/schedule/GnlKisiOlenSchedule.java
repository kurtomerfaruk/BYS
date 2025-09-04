package tr.bel.gaziantep.bysweb.moduls.genel.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ejb.Schedule;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.VefatEdenKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.controller.MezarlikSorgulaService;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEden;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEdenRoot;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.08.2025 10:49
 */
@Stateless
@Slf4j
public class GnlKisiOlenSchedule implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 4101141273409122213L;

    @Inject
    private GnlKisiService service;
    @Inject
    private InitApp initApp;

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "5", dayOfWeek = "Mon-Fri", persistent = false)
    public void updateGnlKisi() throws Exception {
        System.out.println("Mernis Kayit Guncelle baslama tarihi :" + new Date());
        listUpdate();
        System.out.println("Mernis Kayit Guncellebitis tarihi:" + new Date());
    }

    private void listUpdate() throws JsonProcessingException {
        LocalDate startDate = LocalDate.now().minusDays(15);
        LocalDate endDate = LocalDate.now().minusDays(5);
        MezarlikSorgulaService mezarlikSorgulaService = new MezarlikSorgulaService();
        VefatEdenRoot vefatEdenRoot = mezarlikSorgulaService.vefatEdenSorgula(initApp.getProperty("webServisLink"),
                initApp.getProperty("webServisToken"),
                DateUtil.localdateToString(startDate, "dd.MM.yyyy"),
                DateUtil.localdateToString(endDate, "dd.MM.yyyy")
        );

        List<VefatEdenKisi> vefatEdenKisiList = new ArrayList<>();
        if (vefatEdenRoot.getHata() == null) {
            for (VefatEden vefatEden : vefatEdenRoot.getListe()) {
                LocalDate tarih = vefatEden.getOlumTarihi() == null ? vefatEden.getDefinTarihi().toLocalDate() : vefatEden.getOlumTarihi().toLocalDate();
                vefatEdenKisiList.add(new VefatEdenKisi(vefatEden.getTcKimlikNo(), tarih));
            }
        }

        if (!vefatEdenKisiList.isEmpty()) {
            List<String> tcList = vefatEdenKisiList.stream().map(VefatEdenKisi::getTcKimlikNo).collect(Collectors.toList());
            List<GnlKisi> kisilers = service.findByTcKimlikNoListToList(tcList);
            for (GnlKisi kisi : kisilers) {

                VefatEdenKisi vefatEdenKisi =
                        vefatEdenKisiList.stream().filter(x -> x.getTcKimlikNo().equals(kisi.getTcKimlikNo())).findFirst().orElse(null);
                if (vefatEdenKisi == null) continue;
                kisi.setOlumTarihi(vefatEdenKisi.getOlumTarihi());
                kisi.setDurum(EnumGnlDurum.OLU);
                service.edit(kisi);
            }
        }

    }
}
