package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyArama;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyAramaService;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyKisiGrupService;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyKisiGunService;

import java.io.Serial;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.11.2025 13:47
 */
@Named
@ViewScoped
@Slf4j
public class AyAramaController extends AbstractController<AyArama> {

    @Serial
    private static final long serialVersionUID = -2636294070958377264L;

    @Inject
    private AyAramaService ayAramaService;
    @Inject
    private AyKisiGunService ayKisiGunService;
    @Inject
    private AyKisiGrupService ayKisiGrupService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private LocalDate date;
    @Getter
    @Setter
    private EnumGnlGun day;
    @Getter
    @Setter
    private EnumAyGrup group;
    @Getter
    @Setter
    private List<AyArama> ayAramaList;


    public AyAramaController() {
        super(AyArama.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            case GNLGUN -> {
                return filterOptionService.getGnlGuns();
            }
            case AYGRUP -> {
                return filterOptionService.getAyGrups();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void prepareList() {
        date = LocalDate.now().plusDays(1);
        ayAramaList = new ArrayList<>();
    }


    public void changeDay() {
        if (group == null || day == null) return;
        List<AyKisi> ayKisiList = ayKisiGunService.findByGun(day);
        List<AyArama> list = ayAramaService.findByGunByTarihByGrup(day, date, group);
        ayAramaList = new ArrayList<>();
        ayKisiList.forEach(ayKisi ->
                ayAramaList.add(AyArama.builder()
                        .tarih(date)
                        .gun(day)
                        .grup(group)
                        .ayKisi(ayKisi)
                        .build())
        );
        ayAramaList.removeIf(x -> list.stream().anyMatch(y -> y.getAyKisi().equals(x.getAyKisi())));
    }

    public void changeGroup() {
        if (group == null || day == null) return;
        List<AyKisi> ayKisiList = ayKisiGrupService.findByGroup(group);
        List<AyArama> list = ayAramaService.findByGunByTarihByGrup(day, date, group);
        ayAramaList = new ArrayList<>();
        ayKisiList.forEach(ayKisi ->
                ayAramaList.add(AyArama.builder()
                        .tarih(date)
                        .gun(day)
                        .grup(group)
                        .ayKisi(ayKisi)
                        .build())
        );
        ayAramaList.removeIf(x -> list.stream().anyMatch(y -> y.getAyKisi().equals(x.getAyKisi())));
    }

    public void call(AyArama ayArama, boolean come) {
        try {
            if (ayArama != null) {
                ayArama.setGelecek(come);
                ayAramaService.edit(ayArama);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                ayAramaList.remove(ayArama);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void secilenAyKisi(SelectEvent<AyKisi> event) {
        AyKisi ayKisi = event.getObject();
        this.getSelected().setAyKisi(ayKisi);
    }
}
