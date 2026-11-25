package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtTalepIslem;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalep;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtTalepDurumGecmisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtTalepWorkflowService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author OmerFarukKurt
 * @version 1.30.0
 * @since 24.11.2026 16:01
 */
@Named
@ViewScoped
@Slf4j
public class OrtTalepController extends AbstractController<OrtTalep> {

    @Serial
    private static final long serialVersionUID = -6479112138153571653L;

    @Inject
    private OrtTalepService service;
    @Inject
    private OrtTalepWorkflowService workflowService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<OrtTalepDurumGecmisi> statusHistory = Collections.emptyList();
    @Getter
    @Setter
    private Set<EnumOrtTalepIslem> availableActions = Collections.emptySet();
    @Getter
    @Setter
    private String transitionDescription;
    @Getter
    @Setter
    private String pendingAction;

    public OrtTalepController() {
        super(OrtTalep.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTTALEP_DURUMU -> {
                return filterOptionService.getOrtTalepDurumus();
            }
            case ORTTALEP_TIPI -> {
                return filterOptionService.getOrtTalepTipis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public OrtTalep prepareCreate(ActionEvent event) {
        OrtTalep newItem = super.prepareCreate(event);
        newItem.setTalepTarihi(LocalDateTime.now());
        newItem.setDurum(EnumOrtTalepDurumu.YENI_TALEP);
        statusHistory = Collections.emptyList();
        return newItem;
    }

    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            if (this.getSelected().getOrtKisi() == null) {
                FacesUtil.addErrorMessage("Lütfen talep için kişi seçiniz.");
                return;
            }
            service.createTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            if (!isValidationFailed()) {
                super.setItems(null);
                super.setLazyItems(null);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            return;
        }
        try {
            if (this.getSelected().getOrtKisi() == null) {
                FacesUtil.addErrorMessage("Lütfen talep için kişi seçiniz.");
                return;
            }
            service.updateTalep(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        if (this.getSelected() != null && this.getSelected().getId() != null) {
            // Detay her açıldığında talebi veritabanından yenile; randevu gibi
            // başka bir akış durum değiştirmiş olsa bile güncel durum görünsün.
            this.setSelected(service.find(this.getSelected().getId()));
        }
        if (this.getSelected() != null) {
            statusHistory = workflowService.getStatusHistory(this.getSelected());
            availableActions = workflowService.getAvailableActions(this.getSelected());
            pendingAction = workflowService.getPendingAction(this.getSelected().getDurum());
            transitionDescription = null;
        }
    }

    /**
     * Merkezi workflow üzerinden durum geçişi uygular. XHTML bu metodu çağırır;
     * durum doğrudan değiştirilmez.
     */
    public void applyTransition(EnumOrtTalepIslem action) {
        if (this.getSelected() == null || this.getSelected().getId() == null || action == null) {
            FacesUtil.addErrorMessage("Geçersiz talep veya işlem.");
            return;
        }
        try {
            workflowService.applyTransition(this.getSelected().getId(), action, transitionDescription);
            OrtTalep updated = service.find(this.getSelected().getId());
            this.setSelected(updated);
            FacesUtil.addSuccessMessage(action.getDisplayValue() + " " + Constants.KAYIT_GUNCELLENDI);
            transitionDescription = null;
            readInfo();
        } catch (EJBException ex) {
            FacesUtil.addErrorMessage(ex.getCause().getMessage());
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void selectOrtKisi(SelectEvent<OrtKisi> event) {
        OrtKisi ortKisi = event.getObject();
        if (this.getSelected() != null) {
            this.getSelected().setOrtKisi(ortKisi);
        }
    }

    public void closeTalepSelection(OrtTalep ortTalep) {
        PrimeFaces.current().dialog().closeDynamic(ortTalep);
    }

    public void onRowDblSelect(SelectEvent<OrtTalep> event) {
        closeTalepSelection(event.getObject());
    }
}
