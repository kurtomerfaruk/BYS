package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.stokyonetimi.EnumStSayimDurum;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayim;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StSayimKalem;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoLokasyonService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StSayimService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StStokService;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 10:45
 */
@Named
@ViewScoped
@Slf4j
public class StSayimController extends AbstractController<StSayim> {

    @Serial
    private static final long serialVersionUID = 1202600117621L;

    @Inject
    private StSayimService sayimService;
    @Inject
    private StDepoService stDepoService;
    @Inject
    private StDepoLokasyonService stDepoLokasyonService;
    @Inject
    private StStokService stStokService;

    @Getter
    @Setter
    private StSayimKalem kalem = new StSayimKalem();

    public StSayimController() {
        super(StSayim.class);
    }

//    public List<StDepoLokasyon> getLokasyonItems() {
//        if (getSelected() == null || getSelected().getStDepo() == null
//                || getSelected().getStDepo().getId() == null) {
//            return Collections.emptyList();
//        }
//        Integer depoId = getSelected().getStDepo().getId();
//        return stDepoLokasyonService.findAll().stream()
//                .filter(x -> x.getStDepo() != null && depoId.equals(x.getStDepo().getId()))
//                .collect(Collectors.toList());
//    }


    @Override
    public StSayim prepareCreate(ActionEvent event) {
        StSayim yeni = super.prepareCreate(event);
        if (yeni != null) {
            yeni.setBaslangicTarihi(LocalDateTime.now());
            yeni.setDurum(EnumStSayimDurum.TASLAK);
            yeni.setKalemler(new java.util.ArrayList<>());
        }
        kalem = new StSayimKalem();
        return yeni;
    }

    public void onDepoChange() {
        if (getSelected() != null) {
            getSelected().setStDepoLokasyon(null);
        }
    }

    public void addKalem() {
        if (getSelected() == null) {
            return;
        }
        if (kalem.getStStok() == null || kalem.getStStok().getId() == null) {
            FacesUtil.addExclamationMessage("Sayım kalemi için stok seçin.");
            return;
        }
        if (getSelected().getKalemler() == null) {
            getSelected().setKalemler(new java.util.ArrayList<>());
        }
        kalem.setStSayim(getSelected());
        if (kalem.getSistemMiktari() == null) {
            kalem.setSistemMiktari(java.math.BigDecimal.ZERO);
        }
        getSelected().getKalemler().add(kalem);
        kalem = new StSayimKalem();
        FacesUtil.addSuccessMessage(Constants.KAYIT_EKLENDI);
    }

    public void removeKalem(StSayimKalem item) {
        if (getSelected() == null || item == null) {
            return;
        }
        getSelected().getKalemler().remove(item);
        FacesUtil.addSuccessMessage(Constants.KAYIT_SILINDI);
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            if (getSelected() == null) {
                return;
            }
            StSayim kayit = sayimService.sayimOlustur(getSelected());
            setSelected(kayit);
            FacesUtil.addSuccessMessage(Constants.KAYIT_EKLENDI);
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim olusturulamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void baslat() {
        try {
            sayimService.baslat(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Sayım başlatıldı.");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim baslatilamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void tamamla() {
        try {
            sayimService.tamamlaSayim(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Sayım tamamlandı");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim tamamlanamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void onayla() {
        try {
            sayimService.onayla(getSelected(), this.getSyKullanici());
            FacesUtil.addSuccessMessage("Bu sayım onaylandı.");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim onaylanamadi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void iptalEt() {
        try {
            sayimService.iptalEt(getSelected());
            FacesUtil.addSuccessMessage("Bu sayım iptal edildi.");
            setItems(null);
            setLazyItems(null);
        } catch (IllegalArgumentException ex) {
            FacesUtil.addExclamationMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error("Sayim iptal edilemedi", ex);
            FacesUtil.addErrorMessage(Constants.HATA_OLUSTU);
        }
    }


}
