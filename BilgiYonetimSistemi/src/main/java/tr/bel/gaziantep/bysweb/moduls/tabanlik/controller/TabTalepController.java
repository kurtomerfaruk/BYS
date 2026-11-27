package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepIslem;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.dto.TabTalepAsama;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.*;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 11:47
 */
@Named
@ViewScoped
@Slf4j
public class TabTalepController extends AbstractController<TabTalep> {

    @Serial
    private static final long serialVersionUID = 3410469348087299704L;

    @Inject
    private TabTalepService service;
    @Inject
    private TabTalepWorkflowService workflowService;
    @Inject
    private TabRandevuService randevuService;
    @Inject
    private TabOlcumService olcumService;
    @Inject
    private TabUretimKararService kararService;
    @Inject
    private TabOdemeService odemeService;
    @Inject
    private TabUretimService uretimService;
    @Inject
    private TabTeslimatService teslimatService;
    @Inject
    private TabBelgeService belgeService;

    @Getter
    @Setter
    private List<String> durumOptions = Collections.emptyList();
    @Getter
    private List<TabTalepAsama> processStages = Collections.emptyList();
    @Getter
    private List<TabTalepHareket> statusHistory = Collections.emptyList();
    @Getter
    private Set<EnumTabTalepIslem> availableActions = EnumSet.noneOf(EnumTabTalepIslem.class);
    @Getter
    private Set<EnumTabTalepDurumu> backOptions = EnumSet.noneOf(EnumTabTalepDurumu.class);
    @Getter
    private List<TabRandevu> randevular = Collections.emptyList();
    @Getter
    private List<TabOlcum> olcumler = Collections.emptyList();
    @Getter
    private List<TabUretimKarar> kararlar = Collections.emptyList();
    @Getter
    private List<TabOdeme> odemeler = Collections.emptyList();
    @Getter
    private List<TabUretim> uretimler = Collections.emptyList();
    @Getter
    private List<TabTeslimat> teslimatlar = Collections.emptyList();
    @Getter
    private List<TabBelge> belgeler = Collections.emptyList();
    @Getter
    @Setter
    private String transitionDescription;

    public TabTalepController() {
        super(TabTalep.class);
    }

    @Override
    public TabTalep prepareCreate(ActionEvent event) {
        TabTalep newItem = super.prepareCreate(event);
        newItem.setTalepTarihi(LocalDateTime.now());
        newItem.setDurum(EnumTabTalepDurumu.YENI);
        return newItem;
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        if (this.getSelected().getTabKisi() == null) {
            FacesUtil.addErrorMessage("Talep için kişi seçilmelidir.");
            return;
        }
        try {
            service.createTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            if (!isValidationFailed()) {
                this.setItems(null);
                this.setLazyItems(null);
            }
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            service.updateTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Kişi seçim dialogundan dönen TabKisi kaydı.
     */
    public void secilenTabKisi(SelectEvent<TabKisi> event) {
        TabKisi tabKisi = event.getObject();
        this.getSelected().setTabKisi(tabKisi);
    }


    /**
     * Talep seçim dialogunda seçilen kaydı dialogReturn ile döndürür.
     */
    public void closeSelection(TabTalep tabTalep) {
        PrimeFaces.current().dialog().closeDynamic(tabTalep);
    }

    /**
     * Talep seçim tablosunda satıra çift tıklanınca seçim dialogunu kapatır.
     */
    public void onRowDoubleSelect(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            closeSelection(event.getObject());
        }
    }


    /**
     * Detay dialogu açılmadan önce tüm aşama verilerini tazeler.
     */
    public void readInfo() {
        processStages = Collections.emptyList();
        statusHistory = Collections.emptyList();
        availableActions = EnumSet.noneOf(EnumTabTalepIslem.class);
        backOptions = EnumSet.noneOf(EnumTabTalepDurumu.class);
        randevular = Collections.emptyList();
        olcumler = Collections.emptyList();
        kararlar = Collections.emptyList();
        odemeler = Collections.emptyList();
        uretimler = Collections.emptyList();
        teslimatlar = Collections.emptyList();
        belgeler = Collections.emptyList();
        transitionDescription = null;

        if (this.getSelected() == null || this.getSelected().getId() == null) {
            return;
        }
        TabTalep taze = service.find(this.getSelected().getId());
        if (taze == null) {
            return;
        }
        this.setSelected(taze);
        statusHistory = service.findHareketByTalep(taze);
        randevular = randevuService.findByTalep(taze);
        olcumler = olcumService.findByTalep(taze);
        kararlar = kararService.findByTalep(taze);
        odemeler = odemeService.findByTalep(taze);
        uretimler = uretimService.findByTalep(taze);
        teslimatlar = teslimatService.findByTalep(taze);
        belgeler = belgeService.findByTalep(taze);

        for (EnumTabTalepIslem action : EnumTabTalepIslem.values()) {
            if (workflowService.isActionAvailable(taze, action)) {
                availableActions.add(action);
            }
        }
        backOptions = taze.getDurum() == null
                ? EnumSet.noneOf(EnumTabTalepDurumu.class)
                : EnumSet.copyOf(taze.getDurum().getPreviousStatuses());
        processStages = buildProcessStages(taze);
    }

    /**
     * Merkezi workflow üzerinden durum geçişi uygular.
     */
    public void applyTransition(EnumTabTalepIslem action) {
        if (action == null || this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya işlem.");
            return;
        }
        try {
            workflowService.applyTransition(this.getSelected().getId(), action, transitionDescription);
            FacesUtil.addSuccessMessage(action.getDisplayValue() + " " + FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    /**
     * Yetkili düzeltme işlemi: önceki aşamaya gerekçeli geri dönüş.
     */
    public void goBack(EnumTabTalepDurumu hedefDurum) {
        if (hedefDurum == null || this.getSelected() == null || this.getSelected().getId() == null) {
            FacesUtil.addErrorMessage("Geçersiz geri dönüş işlemi.");
            return;
        }
        try {
            workflowService.goBack(this.getSelected().getId(), hedefDurum, transitionDescription);
            FacesUtil.addSuccessMessage(FacesUtil.message(Constants.KAYIT_GUNCELLENDI));
            readInfo();
        } catch (EJBException ex) {
            Throwable cause = FacesUtil.getRootCause(ex.getCause());
            FacesUtil.addErrorMessage(cause == null ? Constants.HATA_OLUSTU : cause.getLocalizedMessage());
        } catch (IllegalArgumentException ex) {
            FacesUtil.addErrorMessage(ex.getMessage());
        }
    }

    private List<TabTalepAsama> buildProcessStages(TabTalep talep) {
        String[] adlar = {"Talep", "Randevu", "Ölçüm", "Karar", "Ücret", "Üretim", "Teslimat", "Tamamlandı"};
        List<Set<EnumTabTalepDurumu>> durumKumeleri = List.of(
                EnumSet.of(EnumTabTalepDurumu.YENI),
                EnumSet.of(EnumTabTalepDurumu.RANDEVU_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.OLCUM_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.URETIM_KARARI_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.UCRET_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.URETIM_BEKLIYOR, EnumTabTalepDurumu.URETIMDE),
                EnumSet.of(EnumTabTalepDurumu.TESLIMAT_RANDEVUSU_BEKLIYOR, EnumTabTalepDurumu.TESLIMAT_BEKLIYOR),
                EnumSet.of(EnumTabTalepDurumu.TAMAMLANDI)
        );
        EnumTabTalepDurumu durum = talep.getDurum();
        int aktifIndex = -1;
        for (int i = 0; i < durumKumeleri.size(); i++) {
            if (durumKumeleri.get(i).contains(durum)) {
                aktifIndex = i;
                break;
            }
        }
        List<TabTalepAsama> stages = new ArrayList<>();
        for (int i = 0; i < adlar.length; i++) {
            String css;
            if (durum == EnumTabTalepDurumu.UYGUN_BULUNMADI || durum == EnumTabTalepDurumu.IPTAL_EDILDI) {
                css = "rejected";
            } else if (i < aktifIndex) {
                css = "done";
            } else if (i == aktifIndex) {
                css = "current";
            } else {
                css = "pending";
            }
            stages.add(new TabTalepAsama(adlar[i], css, "", i + 1));
        }
        return stages;
    }
}
