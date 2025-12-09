package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumGirisCikis;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmGirisCikisService;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmKursiyerKursService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.10.2025 15:07
 */
@Named
@ViewScoped
@Slf4j
public class EkmTopluGirisCikisController extends AbstractController<EkmGirisCikis> {

    @Serial
    private static final long serialVersionUID = -6273947725403008338L;

    @Inject
    private EkmGirisCikisService service;
    @Inject
    private EkmKursiyerKursService ekmKursiyerKursService;

    @Getter
    @Setter
    private EnumGirisCikis girisCikis = EnumGirisCikis.GIRIS;
    //    @Getter
//    @Setter
//    private DualListModel<EyKisi> dualEyKisi;
    @Getter
    @Setter
    private List<EkmGirisCikis> ekmGirisCikisList = new ArrayList<>();
    //    @Getter
//    @Setter
    private List<EkmKursiyer> ekmKursiyerList = new ArrayList<>();

    public EkmTopluGirisCikisController() {
        super(EkmGirisCikis.class);
        createNew();
    }

    private void createNew() {
        EkmGirisCikis newItem;
        try {
            newItem = EkmGirisCikis.class.getDeclaredConstructor().newInstance();
            newItem.setGirisTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
    }

    public void createTraineeList() {
        try {
            if (this.getSelected() != null && this.getSelected().getGnlKurs() != null) {
                if (girisCikis.equals(EnumGirisCikis.GIRIS)) {
                    ekmKursiyerList = ekmKursiyerKursService.findByKurs(this.getSelected().getGnlKurs());
                    for (EkmKursiyer ekmKursiyer : ekmKursiyerList) {
                        EkmGirisCikis ekmGirisCikis = ekmGirisCikisList.stream()
                                .filter(x -> x.getEkmKursiyer().equals(ekmKursiyer) && x.getGnlKurs().equals(this.getSelected().getGnlKurs()))
                                .findFirst().orElse(null);
                        if (ekmGirisCikis == null) {
                            ekmGirisCikis=new EkmGirisCikis();
                            ekmGirisCikis.setGirisTarihi(this.getSelected().getGirisTarihi());
                            ekmGirisCikis.setEkmKursiyer(ekmKursiyer);
                            ekmGirisCikisList.add(ekmGirisCikis);
                        }
                    }
                } else {
                    ekmGirisCikisList = service.findByKurs(this.getSelected().getGnlKurs());
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.addErrorMessage("Kursiyer Listesi oluşturulurken hata oluştu.");
        }
    }

    public void removeItem(EkmGirisCikis item) {
        ekmGirisCikisList.remove(item);
        FacesUtil.successMessage("satirSilindi");
    }

    @Override
    public void save(ActionEvent event) {
        try {
            for (EkmGirisCikis ekmGirisCikis : ekmGirisCikisList) {
                if(girisCikis.equals(EnumGirisCikis.GIRIS)) {
                   ekmGirisCikis.setGnlKurs(this.getSelected().getGnlKurs());
                }else{
                    ekmGirisCikis.setCikisTarihi(this.getSelected().getGirisTarihi());
                }
                service.edit(ekmGirisCikis);
            }
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            createNew();
            girisCikis=EnumGirisCikis.GIRIS;
            ekmGirisCikisList.clear();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.KAYIT_EKLENIRKEN_GUNCELLENIRKEN_HATA_OLUSTU);
        }
    }

//    @Override
//    @PostConstruct
//    public void init() {
////        dualEyKisi = new DualListModel<>();
//    }
//
//    public void createKisiList() {
//        if (this.getSelected() != null && (this.getSelected().getGnlKurs() != null)) {
//            if (girisCikis.equals(EnumGirisCikis.GIRIS)) {
//                List<EyKisi> kisiSource = isBasvuruService.getEyKisiList();
//                dualEyKisi = new DualListModel<>(kisiSource, new ArrayList<>());
//            } else {
//                girisCikisList = girisCikisService.findByKurs(this.getSelected().getGnlKurs());
//                List<EyKisi> kisiSource = new ArrayList<>();
////                for (EkmGirisCikis ekmGirisCikis : girisCikisList) {
////                    if (!kisiSource.contains(ekmGirisCikis.getEyKisi())) {
////                        kisiSource.add(ekmGirisCikis.getEyKisi());
////                    }
////                }
//                dualEyKisi = new DualListModel<>(kisiSource, new ArrayList<>());
//            }
//
//        }
//    }
//
//    @Override
//    public void save(ActionEvent event) {
//        try {
//            if (!dualEyKisi.getTarget().isEmpty()) {
//                for (EyKisi kisi : dualEyKisi.getTarget()) {
//
//                    EkmGirisCikis ekmGirisCikis = null;
//                    if (girisCikis.equals(EnumGirisCikis.CIKIS)) {
//                        ekmGirisCikis = getEkmGirisCikis(kisi);
//                        if (ekmGirisCikis == null) {
//                            continue;
//                        }
//                        ekmGirisCikis.setCikisTarihi(LocalDateTime.now());
//                    } else {
//                        ekmGirisCikis = this.getSelected();
////                        ekmGirisCikis.setEyKisi(kisi);
//                    }
//                    girisCikisService.edit(ekmGirisCikis);
//                }
//                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
//                createNew();
//                dualEyKisi = new DualListModel<>();
//            }
//        } catch (Exception ex) {
//            log.error(null, ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//        }
//    }
//
//    private EkmGirisCikis getEkmGirisCikis(EyKisi kisi) {
////        return girisCikisList.stream()
////                .filter(ekmGirisCikis -> ekmGirisCikis.getEyKisi().getId().equals(kisi.getId())
////                        && ekmGirisCikis.getGnlKurs().getId().equals(this.getSelected().getGnlKurs().getId()))
////                .findFirst()
////                .orElse(null);
//        return null;
//    }
}
