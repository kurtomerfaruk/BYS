package tr.bel.gaziantep.bysweb.moduls.tabanlik.controller;

import jakarta.ejb.EJBException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumEvetHayir;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabUretimKarar;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabUretimKararService;

import java.io.Serial;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 15:02
 */
@Named
@ViewScoped
@Slf4j
public class TabUretimKararController extends AbstractController<TabUretimKarar> {
    @Serial
    private static final long serialVersionUID = 4588068725848024947L;

    @Inject
    private TabUretimKararService service;
    @Inject
    private TabTalepWorkflowService workflowService;

    @Getter
    @Setter
    private TabTalep seciliTalep;
    @Getter
    @Setter
    private EnumEvetHayir karar;
    @Getter
    @Setter
    private String gerekce;

    @Getter
    private List<TabUretimKarar> kararlar = Collections.emptyList();

    public TabUretimKararController() {
        super(TabUretimKarar.class);
    }

    public void onTalepSelected(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            this.seciliTalep = event.getObject();
            this.karar = null;
            this.gerekce = null;
            refreshKararlar();
        }
    }

    public void applyDecision() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Karar için talep seçilmelidir.");
            return;
        }
        if (karar == null) {
            FacesUtil.addErrorMessage("Üretim kararı seçilmelidir.");
            return;
        }
        try {
            workflowService.applyDecision(seciliTalep.getId(), karar, gerekce);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            gerekce = null;
            refreshKararlar();
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

    private void refreshKararlar() {
        kararlar = seciliTalep == null ? Collections.emptyList() : service.findByTalep(seciliTalep);
    }
}
