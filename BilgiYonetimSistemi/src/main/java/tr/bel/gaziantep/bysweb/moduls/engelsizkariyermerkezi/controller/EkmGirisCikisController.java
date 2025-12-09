package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmKursiyerKursService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.10.2025 14:23
 */
@Named
@ViewScoped
@Slf4j
public class EkmGirisCikisController extends AbstractController<EkmGirisCikis> {

    @Serial
    private static final long serialVersionUID = 5457986206714063219L;

    @Inject
    private EkmKursiyerKursService ekmKursiyerKursService;


    public EkmGirisCikisController() {
        super(EkmGirisCikis.class);
    }

    @Override
    public EkmGirisCikis prepareCreate(ActionEvent event) {
        EkmGirisCikis newItem;
        try {
            newItem = EkmGirisCikis.class.getDeclaredConstructor().newInstance();
            newItem.setGirisTarihi(LocalDateTime.now());
            EyKisi eyKisi = EyKisi.builder().gnlKisi(new GnlKisi()).build();
            EkmKursiyer kursiyer = new EkmKursiyer();
            kursiyer.setEyKisi(eyKisi);
            newItem.setEkmKursiyer(kursiyer);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
           log.error(null,ex);
        }
        return null;
    }

    public List<EkmKursiyer> getTraineeList(){
        if(this.getSelected()!=null && this.getSelected().getGnlKurs()!=null){
            return ekmKursiyerKursService.findByKurs(this.getSelected().getGnlKurs());
        }
        return Collections.emptyList();
    }


}
