package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlUyruk;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.10.2025 11:08
 */
@Named
@ViewScoped
@Slf4j
public class MeGirisCikisController extends AbstractController<MeGirisCikis> {

    @Serial
    private static final long serialVersionUID = 1086893276041454925L;

    public MeGirisCikisController() {
        super(MeGirisCikis.class);
    }

    @Override
    public MeGirisCikis prepareCreate(ActionEvent event) {
        MeGirisCikis newItem;
        try {
            newItem = MeGirisCikis.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = new GnlKisi();
            kisi.setKayitTarihi(LocalDate.now());
            kisi.setSosyalYardimAliyorMu(Boolean.FALSE);
            kisi.setEklemeYeri(EnumModul.MORAL_EVI);
            kisi.setUyruk(EnumGnlUyruk.TC);
            EyKisi eyKisi = EyKisi.builder()
                    .gnlKisi(kisi)
                    .eyKisiEngelGrubuList(new ArrayList<>())
                    .build();
            newItem.setMeKisi(MeKisi.builder().eyKisi(eyKisi).build());
            newItem.setGirisTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenMeKisi(SelectEvent<MeKisi> event) {
        MeKisi meKisi = event.getObject();
        this.getSelected().setMeKisi(meKisi);
    }
}
