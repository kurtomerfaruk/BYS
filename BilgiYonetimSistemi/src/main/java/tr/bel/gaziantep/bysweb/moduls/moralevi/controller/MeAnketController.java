package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeAnket;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 20.11.2025 15:20
 */
@Named
@ViewScoped
@Slf4j
public class MeAnketController extends AbstractController<MeAnket> {

    @Serial
    private static final long serialVersionUID = -4720427074872508309L;

    @Inject
    private GnlKisiService kisiService;
    @Inject
    private KpsController kpsController;

    public MeAnketController() {
        super(MeAnket.class);
    }

    @Override
    public MeAnket prepareCreate(ActionEvent event) {
        MeAnket newItem;
        try {
            newItem = MeAnket.class.getDeclaredConstructor().newInstance();
            MeKisi meKisi = MeKisi.builder().eyKisi(new EyKisi()).build();
            newItem.setMeKisi(meKisi);
            newItem.setAnketYapilanGnlKisi(new GnlKisi());
            newItem.setTarih(LocalDate.now());
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

    public void getTcKimlik() {
        if (this.getSelected() != null) {

            try {
                String tcKimlikNo = this.getSelected().getAnketYapilanGnlKisi().getTcKimlikNo();
                if (StringUtil.isBlank(tcKimlikNo)) {
                    return;
                }

                LocalDate dogumTarihi = this.getSelected().getAnketYapilanGnlKisi().getDogumTarihi();

                GnlKisi gnlKisi = kisiService.findByTckimlikNo(tcKimlikNo);
                if (gnlKisi == null) {
                    gnlKisi = GnlKisi.builder()
                            .tcKimlikNo(tcKimlikNo)
                            .dogumTarihi(dogumTarihi)
                            .build();
                }

                gnlKisi = kpsController.findByTcKimlikNo(gnlKisi, EnumModul.MORAL_EVI);
                this.getSelected().setAnketYapilanGnlKisi(gnlKisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}
