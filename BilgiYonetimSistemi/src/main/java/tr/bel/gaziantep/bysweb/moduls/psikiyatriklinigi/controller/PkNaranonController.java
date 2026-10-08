package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkAile;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkNaranon;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkNaranonaKatilanAile;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Narano Listesi ekranlari (List/Create/Edit/View).
 * Bir naranoya birden fazla aile secilebilir.
 * Katilan aile kayitlari fiziksel olarak silinmez; secim kaldirildiginde
 * secili=false yapilarak audit (iz surme) amaciyla saklanir.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.23.0
 * @since 29.09.2026 14:01
 */
@Named
@ViewScoped
@Slf4j
public class PkNaranonController extends AbstractController<PkNaranon> {

    @Serial
    private static final long serialVersionUID = -2922540375483031017L;

    public PkNaranonController() {
        super(PkNaranon.class);
    }

    @Override
    public PkNaranon prepareCreate(ActionEvent event) {
        PkNaranon yeniKayit = super.prepareCreate(event);
        if (yeniKayit != null) {
            yeniKayit.setTarih(LocalDateTime.now());
            yeniKayit.setToplantiIcerigi(null);
            if (yeniKayit.getPkNaranonaKatilanAileList() == null) {
                yeniKayit.setPkNaranonaKatilanAileList(new ArrayList<>());
            } else {
                yeniKayit.getPkNaranonaKatilanAileList().clear();
            }
        }
        return yeniKayit;
    }

    /**
     * Ortak pkAileMultiSec diyalogundan secilen aileleri narano listesine ekler.
     * Onceceden cikarilmis (secili=false) bir aile yeniden secilirse ayni kayit
     * tekrar kullanilir ve secili=true yapilir; boylece mukerrer kayit olusmaz.
     */
    public void secilenAileler(SelectEvent<Map<String, Object>> event) {
        PkNaranon naranon = getSelected();
        if (naranon == null || event == null || event.getObject() == null) {
            return;
        }
        Object secilenler = event.getObject().get("selectedList");
        if (!(secilenler instanceof List<?> liste)) {
            return;
        }

        int eklendi = 0;
        int yenidenSecildi = 0;
        int zatenVar = 0;

        for (Object item : liste) {
            if (!(item instanceof PkAile secilenAile) || secilenAile.getId() == null) {
                continue;
            }
            PkNaranonaKatilanAile kayit = findKatilanAile(secilenAile);
            if (kayit == null) {
                kayit = new PkNaranonaKatilanAile();
                kayit.setPkAile(secilenAile);
                kayit.setPkNaranon(naranon);
                kayit.setSecili(true);
                naranon.getPkNaranonaKatilanAileList().add(kayit);
                eklendi++;
            } else if (!kayit.isSecili()) {
                kayit.setSecili(true);
                yenidenSecildi++;
            } else {
                zatenVar++;
            }
        }

        if (eklendi > 0) {
            FacesUtil.addSuccessMessage("Seçilen aileler eklendi");
        } else if (yenidenSecildi > 0) {
            FacesUtil.addSuccessMessage("Seçilen aileler yeniden seçildi");
        } else if (zatenVar > 0) {
            FacesUtil.addExclamationMessage("Seçilen aileler zaten listede");
        }
    }

    /**
     * Aileyi listeden cikarir. Kayit silinmez, secili=false yapilir (audit).
     */
    public void katilanAileCikar(PkNaranonaKatilanAile katilanAile) {
        PkNaranon naranon = getSelected();
        if (naranon == null || katilanAile == null) {
            return;
        }
        try {
            if (naranon.getPkNaranonaKatilanAileList().contains(katilanAile)) {
                katilanAile.setSecili(false);
                FacesUtil.addSuccessMessage("Aile çıkarıldı");
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Ekranda gosterilecek aile kayitlari (secili=true olanlar).
     * secili=false olanlar veritabaninda saklanir ama listelenmez.
     */
    public List<PkNaranonaKatilanAile> getKatilanAileListesi() {
        PkNaranon naranon = getSelected();
        if (naranon == null || naranon.getPkNaranonaKatilanAileList() == null) {
            return new ArrayList<>();
        }
        return naranon.getPkNaranonaKatilanAileList().stream()
                .filter(PkNaranonaKatilanAile::isSecili)
                .collect(Collectors.toList());
    }

    private PkNaranonaKatilanAile findKatilanAile(PkAile aile) {
        PkNaranon naranon = getSelected();
        if (naranon == null || naranon.getPkNaranonaKatilanAileList() == null) {
            return null;
        }
        return naranon.getPkNaranonaKatilanAileList().stream()
                .filter(k -> k.getPkAile() != null
                        && Objects.equals(k.getPkAile().getId(), aile.getId()))
                .findFirst()
                .orElse(null);
    }

    private boolean validateKayit() {
        PkNaranon naranon = getSelected();
        if (naranon == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("kayitBulunamadi"));
            return false;
        }
        if (naranon.getTarih() == null) {
            FacesUtil.addErrorMessage(FacesUtil.message("naranonTarihSecmelisiniz"));
            return false;
        }
        if (getKatilanAileListesi().isEmpty()) {
            FacesUtil.addErrorMessage(FacesUtil.message("naranonEnAzBirAileSecilmeli"));
            return false;
        }
        return true;
    }

    @Override
    public void save(ActionEvent event) {
        if (!validateKayit()) {
            return;
        }
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (!validateKayit()) {
            return;
        }
        super.saveNew(event);
    }

}
