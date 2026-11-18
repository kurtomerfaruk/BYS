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
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransfer;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StTransferKalem;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StTransferService;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 17.11.2026 16:02
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
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private StTransferKalem kalem = new StTransferKalem();

    public StTransferController() {
        super(StTransfer.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case STTRANSFER_DURUM -> {
                return filterOptionService.getStTransferDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public StTransfer prepareCreate(ActionEvent event) {
        StTransfer yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setTalepTarihi(LocalDateTime.now());
            yeni.setDurum(EnumStTransferDurum.TASLAK);
            yeni.setKalemler(new java.util.ArrayList<>());
        }
        kalem = new StTransferKalem();
        return yeni;
    }

    public void addKalem() {
        if (getSelected() == null) {
            return;
        }
        if (kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            FacesUtil.addExclamationMessage("Transfer kalemi için stok seçin");
            return;
        }
        if (kalem.getMiktar() == null || kalem.getMiktar().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            FacesUtil.addExclamationMessage("Miktar sýfýrdan büyük olmalýdýr");
            return;
        }
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new java.util.ArrayList<>());
        }
        kalem.setSevkMiktari(BigDecimal.ZERO);
        kalem.setTeslimMiktari(BigDecimal.ZERO);
        kalem.setStTransfer(getSelected());
        getSelected().getKalemler().add(kalem);
        kalem = new StTransferKalem();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeKalem(StTransferKalem item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getKalemler().remove(item);
        FacesUtil.successMessage(Constants.KAYIT_SILINDI);
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StTransfer kayit = service.transferOlustur(getSelected());
            setSelected(kayit);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer olusturulamadi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private SyKullanici currentUser() {
        return tr.bel.gaziantep.bysweb.core.utils.Util.getSyKullanici();
    }

    public void onayla() {
        try {
            service.onayla(getSelected(), currentUser());
            FacesUtil.addSuccessMessage("Transfer onaylandý");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer onaylanamadi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void reddet() {
        try {
            service.reddet(getSelected(), currentUser());
            FacesUtil.addSuccessMessage("Transfer reddedildi");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer reddedilemedi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void sevkEt() {
        try {
            service.sevkEt(getSelected());
            FacesUtil.addSuccessMessage("Transfer sevkedildi");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer sevk edilemedi", ex);
//            FacesUtil.addErrorMessage(ex.getMessage());
            if (ex.getCause() != null) {
                FacesUtil.addErrorMessage(ex.getCause().getMessage());
            } else {
                FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
            }

        }
    }

    public void teslimEt() {
        try {
            service.teslimEt(getSelected(), currentUser());
            FacesUtil.addSuccessMessage("Transfer teslim edildi.");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer teslim edilemedi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void iptalEt() {
        try {
            service.iptalEt(getSelected());
            FacesUtil.addSuccessMessage("Transfer iptal edildi.");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.warningMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Transfer iptal edilemedi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void onSelectTransfer() {
        // kalemler lazy collection; erisim aninda yuklenir.
    }
}
