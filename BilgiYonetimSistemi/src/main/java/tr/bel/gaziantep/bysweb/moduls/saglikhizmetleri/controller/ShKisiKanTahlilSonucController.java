package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlUyruk;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKanTahlilSonuc;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 13:14
 */
@Named
@ViewScoped
@Slf4j
public class ShKisiKanTahlilSonucController extends AbstractController<ShKisiKanTahlilSonuc> {

    @Serial
    private static final long serialVersionUID = -595302207429143998L;

    public ShKisiKanTahlilSonucController() {
        super(ShKisiKanTahlilSonuc.class);
    }

    @Override
    public ShKisiKanTahlilSonuc prepareCreate(ActionEvent event) {
        ShKisiKanTahlilSonuc newItem;
        try {
            newItem = ShKisiKanTahlilSonuc.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = new GnlKisi();
            kisi.setKayitTarihi(LocalDate.now());
            kisi.setSosyalYardimAliyorMu(Boolean.FALSE);
            kisi.setEklemeYeri(EnumModul.MORAL_EVI);
            kisi.setUyruk(EnumGnlUyruk.TC);
            ShKisi shKisi =  ShKisi.builder().gnlKisi(kisi).build();
            newItem.setTarih(LocalDateTime.now());
            newItem.setShKisi(shKisi);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenShKisi(SelectEvent<ShKisi> event) {
        ShKisi shKisi = event.getObject();
        this.getSelected().setShKisi(shKisi);
    }
}
