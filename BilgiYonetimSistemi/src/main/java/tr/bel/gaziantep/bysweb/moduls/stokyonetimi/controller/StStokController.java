package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBarkod;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokBirimDonusum;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.*;

import java.io.Serial;
import java.math.BigDecimal;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 10:57
 */
@Named
@ViewScoped
@Slf4j
public class StStokController extends AbstractController<StStok> {

    @Serial
    private static final long serialVersionUID = -2934417348583578519L;

    @Inject
    private StStokService service;
    @Inject
    private StBirimService stBirimService;
    @Inject
    private StStokKategoriService stStokKategoriService;
    @Inject
    private StDepoService stDepoService;
    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokBirimDonusumService stStokBirimDonusumService;

    @Getter
    @Setter
    private StStokBarkod barkod = new StStokBarkod();
    @Getter
    @Setter
    private StStokBirimDonusum birimDonusum = new StStokBirimDonusum();

    public StStokController() {
        super(StStok.class);
    }


    @Override
    public StStok prepareCreate(ActionEvent event) {
        StStok newItem = super.prepareCreate(event);
        barkod = new StStokBarkod();
        birimDonusum = new StStokBirimDonusum();
        return newItem;
    }

    public void addBarkod() {
        if (getSelected() == null) {
            return;
        }
        String barkodText = barkod.getBarkod();
        if (barkodText == null || barkodText.isBlank()) {
            FacesUtil.warningMessage("stBarkodZorunlu");
            return;
        }
        barkod.setBarkod(barkodText.trim());
        barkod.setStStok(getSelected());
        if (getSelected().getStokBarkodList().isEmpty()) {
            barkod.setAnaBarkod(true);
        }
        getSelected().getStokBarkodList().add(barkod);
        barkod = new StStokBarkod();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeBarkod(StStokBarkod item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getStokBarkodList().remove(item);
        if (item.getId() != null) {
            try {
                item.setAktif(false);
                stStokBarkodService.edit(item);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
        if (item.isAnaBarkod() && !getSelected().getStokBarkodList().isEmpty()) {
            getSelected().getStokBarkodList().get(0).setAnaBarkod(true);
        }
        FacesUtil.successMessage(Constants.KAYIT_SILINDI);
    }

    public void addBirimDonusum() {
        if (getSelected() == null) {
            return;
        }
        if (birimDonusum.getStBirim() == null || birimDonusum.getStBirim().getId() == null) {
            FacesUtil.warningMessage("stBirimZorunlu");
            return;
        }
        BigDecimal carpan = birimDonusum.getDonusumCarpani();
        if (carpan == null || carpan.compareTo(BigDecimal.ZERO) <= 0) {
            FacesUtil.warningMessage("stDonusumCarpaniGecersiz");
            return;
        }
        if (getSelected().getStBirim() != null
                && birimDonusum.getStBirim().getId().equals(getSelected().getStBirim().getId())) {
            FacesUtil.warningMessage("stBirimTemelOlamaz");
            return;
        }
        birimDonusum.setStStok(getSelected());
        getSelected().getStokBirimDonusumList().add(birimDonusum);
        birimDonusum = new StStokBirimDonusum();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeBirimDonusum(StStokBirimDonusum item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getStokBirimDonusumList().remove(item);
        if (item.getId() != null) {
            try {
                item.setAktif(false);
                stStokBirimDonusumService.edit(item);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
        FacesUtil.successMessage(Constants.KAYIT_SILINDI);
    }
}

