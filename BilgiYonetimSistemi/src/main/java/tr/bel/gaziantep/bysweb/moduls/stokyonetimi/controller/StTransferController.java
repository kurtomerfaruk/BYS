package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.MatchMode;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStTransferDurum;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
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
    private StStokBakiyeService stokBakiyeService;
    @Inject
    private StStokBarkodService stStokBarkodService;
    @Inject
    private StStokService stStokService;
    @Inject
    private StDepoYetkiService depoYetkiService;
    @Inject
    private StDepoController stDepoController;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private StTransferKalem kalem = new StTransferKalem();
    @Getter
    @Setter
    private String barcodeInput;

    @Getter
    private BigDecimal kaynakMevcutMiktar = BigDecimal.ZERO;
    @Getter
    private BigDecimal kaynakTransferdekiMiktar = BigDecimal.ZERO;
    @Getter
    private BigDecimal kaynakKullanilabilirMiktar = BigDecimal.ZERO;

    public StTransferController() {
        super(StTransfer.class);
    }

    @PostConstruct
    @Override
    public void init() {
        super.init();
        List<Integer> depoListIds = stDepoController.getItems().stream()
                .map(StDepo::getId)
                .toList();
        this.getFilterMap().put("kaynakDepo", FilterMeta.builder()
                .field("kaynakDepo.id")
                .filterValue(depoListIds)
                .matchMode(MatchMode.IN)
                .build());
        this.getFilterMap().put("hedefDepo", FilterMeta.builder()
                .field("hedefDepo.id")
                .filterValue(depoListIds)
                .matchMode(MatchMode.IN)
                .build());
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
    public void resetParents() {
        barcodeInput = null;
        kalem = new StTransferKalem();
        clearBakiyeBilgi();
    }

    @Override
    public StTransfer prepareCreate(ActionEvent event) {
        StTransfer yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setTalepTarihi(LocalDateTime.now());
            yeni.setDurum(EnumStTransferDurum.TASLAK);
            yeni.setKalemler(new java.util.ArrayList<>());
        }
        resetParents();
        return yeni;
    }

    public void clearBakiyeBilgi() {
        kaynakMevcutMiktar = BigDecimal.ZERO;
        kaynakTransferdekiMiktar = BigDecimal.ZERO;
        kaynakKullanilabilirMiktar = BigDecimal.ZERO;
    }

    public void refreshKalemBakiye() {
        clearBakiyeBilgi();
        if (getSelected() == null || getSelected().getKaynakDepo() == null
                || kalem == null || kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            return;
        }
        StStokBakiye bakiye = stokBakiyeService.find(kalem.getStStok(), getSelected().getKaynakDepo(), null, false);
        if (bakiye == null) {
            return;
        }
        kaynakMevcutMiktar = bakiye.getMiktar() == null ? BigDecimal.ZERO : bakiye.getMiktar();
        kaynakTransferdekiMiktar = bakiye.getTransferdekiMiktar() == null ? BigDecimal.ZERO : bakiye.getTransferdekiMiktar();
        BigDecimal rezerve = bakiye.getRezerveMiktar() == null ? BigDecimal.ZERO : bakiye.getRezerveMiktar();
        BigDecimal kullanilabilir = kaynakMevcutMiktar.subtract(rezerve).subtract(kaynakTransferdekiMiktar);
        kaynakKullanilabilirMiktar = kullanilabilir.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : kullanilabilir;
    }

    public void onBarcodeInput() {
        if (barcodeInput == null || barcodeInput.isBlank()) {
            return;
        }
        String code = barcodeInput.trim();
        StStok stok = null;
        StStokBarkod barkod = stStokBarkodService.findByBarkod(code);
        if (barkod != null && barkod.getStStok() != null) {
            stok = barkod.getStStok();
        }
        if (stok == null) {
            stok = stStokService.findByKod(code);
        }
        if (stok == null) {
            FacesUtil.addExclamationMessage("Bu barkod/kod ile kayıtlı stok bulunamadı.");
            return;
        }
        kalem.setStStok(stok);
        refreshKalemBakiye();
    }

    public String getKalemBirimAdi() {
        if (kalem == null || kalem.getStStok() == null || kalem.getStStok().getStBirim() == null) {
            return "";
        }
        return kalem.getStStok().getStBirim().getAd();
    }


    public void addKalem() {
        if (getSelected() == null) {
            return;
        }
        if (kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            FacesUtil.addExclamationMessage("Transfer kalemi için stok seçin");
            return;
        }
        if (kalem.getMiktar() == null || kalem.getMiktar().compareTo(BigDecimal.ZERO) <= 0) {
            FacesUtil.addExclamationMessage("Miktar sıfırdan büyük olmalıdır");
            return;
        }
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new java.util.ArrayList<>());
        }
        StTransferKalem mevcut = getSelected().getKalemler().stream()
                .filter(item -> item.getStStok() != null && item.getStStok().getId() != null)
                .filter(item -> item.getStStok().getId().equals(kalem.getStStok().getId()))
                .findFirst()
                .orElse(null);

        if (mevcut != null) {
            BigDecimal mevcutMiktar = mevcut.getMiktar() == null ? BigDecimal.ZERO : mevcut.getMiktar();
            mevcut.setMiktar(mevcutMiktar.add(kalem.getMiktar()));
        } else {
            kalem.setSevkMiktari(BigDecimal.ZERO);
            kalem.setTeslimMiktari(BigDecimal.ZERO);
            kalem.setStTransfer(getSelected());
            getSelected().getKalemler().add(kalem);
        }

        kalem = new StTransferKalem();
        barcodeInput = null;
        clearBakiyeBilgi();
        kalem = new StTransferKalem();
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeKalem(StTransferKalem item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getKalemler().remove(item);
        FacesUtil.addSuccessMessage("Kalem silindi...");
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
        } catch (EJBException ex) {
            FacesUtil.addExclamationMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error("Transfer olusturulamadi", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void onayla() {
        try {
            service.onayla(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Transfer onaylandı");
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
            service.reddet(getSelected(), this.getSyKullanici());
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
            service.sevkEt(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Transfer sevk edildi");
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
            service.teslimEt(getSelected(), this.getSyKullanici());
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
        barcodeInput = null;
        kalem = new StTransferKalem();
        clearBakiyeBilgi();
    }

    public boolean isHedefDepoYetkili() {
        if (getSelected() == null || getSelected().getHedefDepo() == null || getSyKullanici() == null) {
            return false;
        }
        if (getSyKullanici().getKullaniciTuru() == EnumSyKullaniciTuru.SISTEM_YONETICISI) {
            return true;
        }
        return depoYetkiService.findByKullaniciByDepo(getSyKullanici(), getSelected().getHedefDepo()) != null;
    }


    public void prepareSevk() {
        if (getSelected() != null) {
            getSelected().setSevkEdenKisi(null);
            getSelected().setSevkAciklama(null);
        }
    }

    public void prepareTeslim() {
        if (getSelected() != null) {
            getSelected().setTeslimAlanKisi(null);
            getSelected().setTeslimAciklama(null);
        }
    }
}
