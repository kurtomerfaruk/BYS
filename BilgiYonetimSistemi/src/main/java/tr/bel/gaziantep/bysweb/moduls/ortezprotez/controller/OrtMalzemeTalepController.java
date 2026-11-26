package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeDurumu;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtMalzemeTalepDetay;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtMalzemeTalepService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 25.11.2026 16:25
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
    private OrtTalepController ortTalepController;

    @Getter
    @Setter
    private String talepAciklama;
    @Getter
    @Setter
    private StStok seciliStok;
    @Getter
    @Setter
    private StDepo seciliDepo;
    @Getter
    @Setter
    private BigDecimal miktar;
    @Getter
    @Setter
    private String kalemAciklama;
    @Getter
    @Setter
    private String redGerekcesi;

    @Getter
    private List<OrtMalzemeTalepDetay> yeniKalemler = new ArrayList<>();
    @Getter
    private List<OrtMalzemeTalep> talepler = Collections.emptyList();
    @Getter
    private List<OrtMalzemeTalepDetay> seciliDetaylar = Collections.emptyList();

    public OrtMalzemeTalepController() {
        super(OrtMalzemeTalep.class);
    }

    private OrtTalep currentTalep() {
        return ortTalepController == null ? null : ortTalepController.getSelected();
    }

    public void loadForRequest() {
        talepler = service.findByTalep(currentTalep());
        yeniKalemler = new ArrayList<>();
        talepAciklama = null;
        seciliStok = null;
        seciliDepo = null;
        miktar = null;
        kalemAciklama = null;
    }

    public void addItem() {
        if (seciliStok == null || seciliDepo == null) {
            FacesUtil.addErrorMessage("Malzeme ve depo seçilmelidir.");
            return;
        }
        if (miktar == null || miktar.compareTo(BigDecimal.ZERO) <= 0) {
            FacesUtil.addErrorMessage("Miktar sıfırdan büyük olmalıdır.");
            return;
        }
        yeniKalemler.add(OrtMalzemeTalepDetay.builder()
                .stStok(seciliStok)
                .stDepo(seciliDepo)
                .istenenMiktar(miktar)
                .aciklama(kalemAciklama)
                .build());
        seciliStok = null;
        seciliDepo = null;
        miktar = null;
        kalemAciklama = null;
    }

    public void removeItem(OrtMalzemeTalepDetay detay) {
        yeniKalemler.remove(detay);
    }

    public void createRequest() {
        try {
            OrtMalzemeTalep mt = service.createRequest(currentTalep(), talepAciklama, yeniKalemler);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            loadForRequest();
            refreshDetail();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void approve(OrtMalzemeTalep mt) {
        try {
            service.approve(mt.getId(), Util.getSyKullanici(), mt.getAciklama());
            FacesUtil.successMessage("Malzeme talebi onaylandı ve stok rezerve edildi.");
            loadForRequest();
            refreshDetail();
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void reject(OrtMalzemeTalep mt) {
        try {
            service.reject(mt.getId(), Util.getSyKullanici(), redGerekcesi);
            FacesUtil.successMessage("Malzeme talebi reddedildi.");
            redGerekcesi = null;
            loadForRequest();
            refreshDetail();
        } catch (EJBException ex) {
            FacesUtil.addErrorMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void showDetails(OrtMalzemeTalep mt) {
        seciliDetaylar = service.findDetails(mt);
    }

    public boolean canApprove() {
        return service.canApprove();
    }

    private void refreshDetail() {
        if (ortTalepController != null) {
            ortTalepController.readInfo();
        }
    }

    public boolean hasPending() {
        return talepler.stream().anyMatch(t -> t.getDurum() == EnumOrtMalzemeDurumu.BEKLIYOR);
    }

    public boolean allApproved() {
        return !talepler.isEmpty() && talepler.stream()
                .allMatch(t -> t.getDurum() == EnumOrtMalzemeDurumu.ONAYLANDI);
    }
}
