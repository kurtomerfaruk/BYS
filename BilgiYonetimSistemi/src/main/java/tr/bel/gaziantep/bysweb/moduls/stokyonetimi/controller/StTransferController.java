package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.*;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 16.11.2026 10:09
 */
@Named
@ViewScoped
@Slf4j
public class StTransferController extends AbstractController<StTransfer> {

    @Serial
    private static final long serialVersionUID = 8834037836498684165L;

    @Inject
    private StTransferService service;
    @Inject
    private StTransferIslemService transferIslemService;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private StStokService stokService;
    @Inject
    private StStokHareketService birimService;
    @Inject
    private StDepoLokasyonService lokasyonService;


    @Getter
    @Setter
    private StStok stStok;
    @Getter
    @Setter
    private BigDecimal amount;
    @Getter
    @Setter
    private StStokHareket unit;
    @Getter
    @Setter
    private StDepoLokasyon sourceLocation;
    @Getter
    @Setter
    private StDepoLokasyon targetLocation;

    public StTransferController() {
        super(StTransfer.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STTRANSFER_DURUM -> {
                return filterOptionService.getStTranferDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public StTransfer prepareCreate(ActionEvent event) {
        StTransfer transfer = super.prepareCreate(event);
        if (transfer != null) {
            transfer.setDurum(EnumStTransferDurum.TASLAK);
            transfer.setTalepTarihi(LocalDateTime.now());
            transfer.setTalepEdenSyKullanici(Util.getSyKullanici());
            transfer.setTransferNo(service.generateTransferNo());
        }
        return transfer;
    }

    public void confirm() {
        try {
            if (this.getSelected() != null) {
                transferIslemService.confirm(this.getSelected(), Util.getSyKullanici());
                FacesUtil.addSuccessMessage("Transfer onaylandý");
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("Hata oluþtu");
        }
    }

    public void dispatch() {
        try {
            if (this.getSelected() != null) {
                transferIslemService.dispatch(this.getSelected(), Util.getSyKullanici());
                FacesUtil.addSuccessMessage("Transfer sevk edildi");
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("Hata oluþtu");
        }
    }

    public void pickUp() {
        try {
            if (this.getSelected() != null) {
                transferIslemService.pickUp(this.getSelected(), Util.getSyKullanici());
                FacesUtil.addSuccessMessage("Transfer teslim alýndý");
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("Hata oluþtu");
        }
    }

    public void cancel() {
        try {
            if (this.getSelected() != null) {
                transferIslemService.cancel(this.getSelected());
                FacesUtil.addSuccessMessage("Transfer iptal edildi");
                this.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage("Hata oluþtu");
        }
    }

    public void addItem() {
        if (this.getSelected() == null || stStok == null || amount == null) return;
        StTransferKalem kalem = new StTransferKalem();
        kalem.setStTransfer(this.getSelected());
        kalem.setStStok(stStok);
        kalem.setMiktar(amount);
        kalem.setStBirim(unit != null ? unit : stStok.getStBirim());
        kalem.setKaynakStDepoLokasyon(sourceLocation);
        kalem.setHedefStDepoLokasyon(targetLocation);
        kalem.setSevkMiktar(java.math.BigDecimal.ZERO);
        kalem.setTeslimMiktar(java.math.BigDecimal.ZERO);
        if (this.getSelected().getStTransferKalemList() == null) {
            this.getSelected().setStTransferKalemList(new java.util.ArrayList<>());
        }
        this.getSelected().getStTransferKalemList().add(kalem);
        stStok = null;
        amount = null;
        unit = null;
        sourceLocation = null;
        targetLocation = null;
    }

    public void removeItem(StTransferKalem kalem) {
        if (this.getSelected() != null && this.getSelected().getStTransferKalemList() != null) {
            this.getSelected().getStTransferKalemList().remove(kalem);
        }
    }
}
