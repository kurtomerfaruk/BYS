package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStokHareket;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokHareketService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokIslemService;

import java.io.Serial;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 08:49
 */
@Named
@ViewScoped
@Slf4j
public class StStokHareketController extends AbstractController<StStokHareket> {

    @Serial
    private static final long serialVersionUID = -6610720030443546883L;

    @Inject
    private StStokHareketService service;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private StStokIslemService islemService;

    public StStokHareketController() {
        super(StStokHareket.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STHAREKET_TURU -> {
                return filterOptionService.getStHareketTurus();
            }
            case GIRIS_CIKIS -> {
                return filterOptionService.getGirisCikis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public StStokHareket prepareCreate(ActionEvent event) {
        StStokHareket hareket = super.prepareCreate(event);
        if (hareket != null) {
            hareket.setIslemTarihi(LocalDateTime.now());
            hareket.setIptalEdildi(false);
            hareket.setSyKullanici(this.getSyKullanici());
            hareket.setHareketNo(service.generateHareketNo());
        }
        return hareket;
    }

    public void createMovement(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                islemService.createMovement(this.getSelected());
                FacesUtil.addSuccessMessage("Stok hareketi kaydedildi");
                this.setSelected(null);
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void updateMovement(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                islemService.updateMovement(this.getSelected());
                FacesUtil.addSuccessMessage("Stok hareketi güncellendi");
                this.setSelected(null);
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void calculate() {
        if (this.getSelected() == null) return;
        BigDecimal amount = this.getSelected().getMiktar() != null ? this.getSelected().getMiktar() : BigDecimal.ZERO;
        BigDecimal unitPrice = this.getSelected().getBirimMaliyet() != null ? this.getSelected().getBirimMaliyet() : BigDecimal.ZERO;
        this.getSelected().setToplamMaliyet(amount.multiply(unitPrice).setScale(4, RoundingMode.HALF_UP));
    }

    public void cancel(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                islemService.cancel(this.getSelected());
                FacesUtil.addSuccessMessage("Stok hareketi iptal edildi");
                this.setSelected(null);
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


}
