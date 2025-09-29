package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractKisiController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:08
 */
@Named
@ViewScoped
@Slf4j
public class OrtHastaController extends AbstractKisiController<OrtHasta> {

    @Serial
    private static final long serialVersionUID = 1669461090844928292L;

    @Inject
    private OrtPersonelService personelService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private OrtPersonel ortPersonel;

    public OrtHastaController() {
        super(OrtHasta.class);
    }


    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTENGEL_OLUSUM -> {
                return filterOptionService.getOrtEngelOlusums();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    @PostConstruct
    public void init(){
        super.init();
        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
        if(ortPersonel==null){
            FacesUtil.addErrorMessage("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
        }
    }

    @Override
    public OrtHasta prepareCreate(ActionEvent event) {
        OrtHasta newItem;
        try {
            newItem = OrtHasta.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .eklemeYeri(EnumModul.ORTEZ_PROTEZ).build();
            newItem.setGnlKisi(kisi);
            newItem.setDegerlendirmeTarihi(LocalDateTime.now());
            newItem.setMuayeneYapanOrtPersonel(ortPersonel);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
//        if (this.getSelected() != null) {
//            try {
//                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
//                LocalDate dogumTarihi = this.getSelected().getGnlKisi().getDogumTarihi();
//                GnlKisi kisi = kisiService.findByTckimlikNoByDogumTarihi(tcKimlikNo, dogumTarihi);
//                if (kisi == null) kisi = this.getSelected().getGnlKisi();
//                kisi.setAktif(Boolean.TRUE);
//                kisi = kps.findByTcKimlikNo(kisi, EnumModul.ORTEZ_PROTEZ);
//                this.getSelected().setGnlKisi(kisi);
//            } catch (Exception ex) {
//                log.error(null, ex);
//                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//            }
//        }
        fetchTcKimlik(
                OrtHasta::getGnlKisi,
                OrtHasta::setGnlKisi,
                EnumModul.ORTEZ_PROTEZ
        );
    }
}
