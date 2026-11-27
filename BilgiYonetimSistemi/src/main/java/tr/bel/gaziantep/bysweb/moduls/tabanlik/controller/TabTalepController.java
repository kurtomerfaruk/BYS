package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.tabanlik.EnumTabTalepDurumu;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabKisi;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepService;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

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

    @Getter
    @Setter
    private List<String> durumOptions = Collections.emptyList();

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
}
