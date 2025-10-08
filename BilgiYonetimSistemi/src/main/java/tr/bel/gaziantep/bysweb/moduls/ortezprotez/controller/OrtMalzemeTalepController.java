package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ErrorType;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtBasvuruService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtMalzemeTalepService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtMalzemeTalepStokService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 13:56
 */
@Named
@ViewScoped
@Slf4j
public class OrtMalzemeTalepController extends AbstractController<OrtMalzemeTalep> {

    @Serial
    private static final long serialVersionUID = 8030620919466467588L;

    @Inject
    private OrtMalzemeTalepService service;
    @Inject
    private OrtMalzemeTalepStokService ortMalzemeTalepStokService;
    @Inject
    private OrtPersonelService personelService;
    @Inject
    private OrtBasvuruService ortBasvuruService;

    @Getter
    @Setter
    private int rowIndex;
    @Getter
    @Setter
    private OrtPersonel ortPersonel;

    public OrtMalzemeTalepController() {
        super(OrtMalzemeTalep.class);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
        if (ortPersonel == null) {
            FacesUtil.addErrorMessage("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
        }
    }

    @Override
    public OrtMalzemeTalep prepareCreate(ActionEvent event) {
        OrtMalzemeTalep newItem;
        try {
            newItem = OrtMalzemeTalep.class.getDeclaredConstructor().newInstance();
            OrtHasta ortHasta = OrtHasta.builder().gnlKisi(new GnlKisi()).build();
            newItem.setOrtBasvuru(OrtBasvuru.builder().ortHasta(ortHasta).build());
            newItem.setTalepTarihi(LocalDateTime.now());
            newItem.setDurum(EnumOrtMalzemeOnayDurumu.BEKLEMEDE);
            newItem.setTalepEdenOrtPersonel(ortPersonel);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenBasvuru(SelectEvent<OrtBasvuru> event) {
        OrtBasvuru basvuru = event.getObject();
        this.getSelected().setOrtBasvuru(basvuru);
    }

    public void onAddNew() {
        if (this.getSelected() != null) {
            List<OrtMalzemeTalepStok> detays = this.getSelected().getOrtMalzemeTalepStokList();
            if (detays == null) detays = new ArrayList<>();
            OrtMalzemeTalepStok talepStok = OrtMalzemeTalepStok.builder()
                    .ortStok(new OrtStok())
                    .ortMalzemeTalep(this.getSelected())
                    .miktar(BigDecimal.ONE)
                    .build();
            detays.add(talepStok);
            this.getSelected().setOrtMalzemeTalepStokList(detays);
        }
    }

    public void onCellEdit(CellEditEvent event) {
    }

    public void onCellEditInit(CellEditEvent event) {
        this.setRowIndex(event.getRowIndex());
    }

    public void removeRow(OrtMalzemeTalepStok detay) {
        try {
            this.getSelected().getOrtMalzemeTalepStokList().removeIf(x -> x.getOrtStok().equals(detay.getOrtStok()));

            if (this.getSelected().getId() != null && detay.getId() != null) {
                detay.setAktif(false);
                ortMalzemeTalepStokService.edit(detay);
            }
            this.getSelected().getOrtMalzemeTalepStokList().removeIf(x -> x.getOrtStok().equals(detay.getOrtStok()));
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void secilenStok(SelectEvent<OrtStok> event) {
        OrtStok stok = event.getObject();
        if (this.getSelected() != null) {
            OrtMalzemeTalepStok existingDetay = this.getSelected().getOrtMalzemeTalepStokList()
                    .stream()
                    .filter(s -> s.getOrtStok().equals(stok))
                    .findFirst()
                    .orElse(null);
            if (existingDetay != null) {
                FacesUtil.warningMessage("secilenStokZatenVar");
                return;
            }
            this.getSelected().getOrtMalzemeTalepStokList().get(rowIndex).setOrtStok(stok);
        }
    }

    public void persist(ActionEvent event) {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        try {
            if (this.getSelected().getOrtMalzemeTalepStokList().isEmpty()) {
                FacesUtil.warningMessage("malzemeGirilmedenKaydedilemez");
                return;
            }

            service.persist(this.getSelected(), EnumOrtBasvuruHareketDurumu.MALZEME_TALEP_EDILDI);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void update(ActionEvent event) {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        try {
            if (this.getSelected().getOrtMalzemeTalepStokList().isEmpty()) {
                FacesUtil.warningMessage("malzemeGirilmedenKaydedilemez");
                return;
            }
            EnumOrtBasvuruHareketDurumu durum = EnumOrtBasvuruHareketDurumu.MALZEME_TALEP_EDILDI;
            if (this.getSelected().getDurum() == EnumOrtMalzemeOnayDurumu.IPTAL_EDILDI) {
                durum = EnumOrtBasvuruHareketDurumu.MALZEME_TALEBI_IPTAL_EDILDI;
            }
            service.update(this.getSelected(), this.getSyKullanici(), durum);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void approveInfo() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        this.getSelected().setOnaylayanOrtPersonel(ortPersonel);
        this.getSelected().setOnayTarihi(LocalDateTime.now());
    }

    public void rejectInfo() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        this.getSelected().setRededenOrtPersonel(ortPersonel);
        this.getSelected().setRedTarihi(LocalDateTime.now());
    }

    public void approve() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            this.getSelected().setRedTarihi(null);
            this.getSelected().setRedSebebi(null);
            this.getSelected().setRedTarihi(null);
            this.getSelected().setDurum(EnumOrtMalzemeOnayDurumu.ONAYLANDI);
            service.approve(this.getSelected(), this.getSyKullanici(), EnumOrtBasvuruHareketDurumu.MALZEME_TALEBI_ONAYLANDI);
            FacesUtil.successMessage("kayitOnaylandi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void reject() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            this.getSelected().setOnayTarihi(null);
            this.getSelected().setOnaylayanOrtPersonel(null);
            this.getSelected().setDurum(EnumOrtMalzemeOnayDurumu.REDDEDILDI);
            service.edit(this.getSelected());
            OrtBasvuru basvuru = this.getSelected().getOrtBasvuru();
            basvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.MALZEME_TALEBI_REDDEDILDI);
            ortBasvuruService.edit(basvuru);
            FacesUtil.successMessage("kayitRededildi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void deliveryInfo() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        this.getSelected().setTeslimTarihi(LocalDateTime.now());
        this.getSelected().setTeslimEdenPersonel(ortPersonel);
    }

    public void delivery() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            this.getSelected().setTeslimEdildi(true);
            service.edit(this.getSelected());
            OrtBasvuru basvuru = this.getSelected().getOrtBasvuru();
            basvuru.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.TEKNIKERE_TESLIM_EDILDI);
            ortBasvuruService.edit(basvuru);
            FacesUtil.successMessage("malzemeTeslimEdildi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


}
