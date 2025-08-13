package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlUyruk;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKontrol;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.07.2025 12:44
 */
@Named
@ViewScoped
@Slf4j
public class ShKisiKontrolController extends AbstractController<ShKisiKontrol> {

    @Serial
    private static final long serialVersionUID = -4324584000987523742L;

    public ShKisiKontrolController() {
        super(ShKisiKontrol.class);
    }

    @Override
    public ShKisiKontrol prepareCreate(ActionEvent event) {
        ShKisiKontrol newItem;
        try {
            newItem = ShKisiKontrol.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = new GnlKisi();
            kisi.setKayitTarihi(LocalDate.now());
            kisi.setSosyalYardimAliyorMu(Boolean.FALSE);
            kisi.setEklemeYeri(EnumModul.SAGLIK_HIZMETLERI);
            kisi.setUyruk(EnumGnlUyruk.TC);
            ShKisi shKisi = ShKisi.builder().gnlKisi(kisi).build();
            newItem.setShKisi(shKisi);
            newItem.setTarih(LocalDate.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getBki() {
        try {
            if (this.getSelected().getKilo() != null && this.getSelected().getBoy() != null) {
                double boy = this.getSelected().getBoy().doubleValue();
                double kilo = this.getSelected().getKilo().doubleValue();

                if (boy > 0) {
                    boy = boy / 100;
                }

                double bki = kilo / (Math.pow(boy, 2));
                this.getSelected().setBki(new BigDecimal(bki).setScale(2, RoundingMode.HALF_UP));
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void secilenShKisi(SelectEvent<ShKisi> event) {
        ShKisi shKisi = event.getObject();
        this.getSelected().setShKisi(shKisi);
    }
}
