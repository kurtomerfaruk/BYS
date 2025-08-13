package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyGorusme;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.06.2025 00:07
 */
@Named
@ViewScoped
@Slf4j
public class EyGorusmeController extends AbstractController<EyGorusme> {

    @Serial
    private static final long serialVersionUID = 7161231176168535590L;


    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kps;

    public EyGorusmeController() {
        super(EyGorusme.class);
    }

    @Override
    public EyGorusme prepareCreate(ActionEvent event) {
        EyGorusme newItem;
        try {
            newItem = EyGorusme.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .sosyalYardimAliyorMu(false)
                    .eklemeYeri(EnumModul.ENGELSIZLER)
                    .build();
            newItem.setGnlKisi(kisi);
            newItem.setGorusmeTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
        try {
            if (this.getSelected() != null) {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kps.findByTcKimlikNo(kisi, EnumModul.ENGELSIZLER);
                if (kisi != null) {
                    this.getSelected().setGnlKisi(kisi);
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
    }
}
