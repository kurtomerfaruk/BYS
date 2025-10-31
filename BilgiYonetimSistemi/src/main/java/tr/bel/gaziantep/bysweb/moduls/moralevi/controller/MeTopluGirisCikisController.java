package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.DualListModel;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeGirisCikisService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeKisiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.10.2025 11:18
 */
@Named
@ViewScoped
@Slf4j
public class MeTopluGirisCikisController extends AbstractController<MeGirisCikis> {

    @Serial
    private static final long serialVersionUID = -9202110063391466216L;

    @Inject
    private MeGirisCikisService service;
    @Inject
    private MeKisiService meKisiService;

    private DualListModel<MeKisi> dualMeKisiler;
    @Getter
    @Setter
    private List<MeGirisCikis> cikisYapacaklar;

    public MeTopluGirisCikisController() {
        super(MeGirisCikis.class);
        newInstance();
    }

    public DualListModel<MeKisi> getDualMeKisiler() {
        return dualMeKisiler;
    }

    public void setDualMeKisiler(DualListModel<MeKisi> dualMeKisiler) {
        this.dualMeKisiler = dualMeKisiler;
    }

    public MeGirisCikis newInstance() {
        MeGirisCikis newItem;
        try {
            newItem = MeGirisCikis.class.getDeclaredConstructor().newInstance();
            newItem.setGirisTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    @Override
    @PostConstruct
    public  void init(){
        super.init();
        createList();
    }

    private void createList() {
        dualMeKisiler = new DualListModel<>();
        List<MeKisi> kisiSource = meKisiService.findByGirisYapmayanlar();
        List<MeKisi> kisiTarget = new ArrayList<>();
        dualMeKisiler = new DualListModel<>(kisiSource, kisiTarget);
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) {

            try {
                if (!dualMeKisiler.getTarget().isEmpty()) {
                    for (MeKisi kisi : dualMeKisiler.getTarget()) {
                        this.getSelected().setMeKisi(kisi);
                        this.getSelected().setCikisTarihi(null);
                        service.create(this.getSelected());
                    }
                    FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                    newInstance();
                    createList();
                }
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    @Override
    public void save(ActionEvent event){
        if (this.getSelected() == null) {

            try {
                for (MeGirisCikis meGirisCikis : cikisYapacaklar) {
                    meGirisCikis.setCikisTarihi(LocalDateTime.now());
                    service.edit(meGirisCikis);
                    FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                }
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void createExit(ComponentSystemEvent event) {
        if (cikisYapacaklar == null) {
            cikisYapacaklar = new ArrayList<>();
            cikisYapacaklar = service.findByEntrance();
            cikisYapacaklar.forEach(meGirisCikis -> meGirisCikis.setCikisTarihi(LocalDateTime.now()));
        }


    }

    public void removeItem(MeGirisCikis item){
        cikisYapacaklar.remove(item);
    }
}
