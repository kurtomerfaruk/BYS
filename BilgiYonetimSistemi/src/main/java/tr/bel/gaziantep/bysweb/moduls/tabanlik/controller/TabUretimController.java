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
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StStok;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabTalep;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.entity.TabUretim;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabTalepWorkflowService;
import tr.bel.gaziantep.bysweb.moduls.tabanlik.service.TabUretimService;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Üretim takip ekranı. Üretim tamamlandığında stok girişi workflow üzerinden yapılır.
 *
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 27.11.2026 16:15
 */
@Named
@ViewScoped
@Slf4j
public class TabUretimController extends AbstractController<TabUretim> {

    @Serial
    private static final long serialVersionUID = -5039182746981739521L;

    @Inject
    private TabUretimService service;
    @Inject
    private TabTalepWorkflowService workflowService;

    @Getter
    @Setter
    private TabTalep seciliTalep;
    @Getter
    @Setter
    private SyKullanici sorumluSyKullanici;
    @Getter
    @Setter
    private StStok stStok;
    @Getter
    @Setter
    private StDepo stDepo;
    @Getter
    @Setter
    private BigDecimal miktar = BigDecimal.ONE;
    @Getter
    @Setter
    private String aciklama;

    @Getter
    private List<TabUretim> uretimler = Collections.emptyList();

    public TabUretimController() {
        super(TabUretim.class);
    }

    public void onTalepSelected(SelectEvent<TabTalep> event) {
        if (event != null && event.getObject() != null) {
            this.seciliTalep = event.getObject();
            this.aciklama = null;
            this.miktar = BigDecimal.ONE;
            refreshUretimler();
        }
    }

    public void startProduction() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Üretim için talep seçilmelidir.");
            return;
        }
        try {
            workflowService.startProduction(seciliTalep.getId(),
                    sorumluSyKullanici == null ? null : sorumluSyKullanici.getId(),
                    stStok == null ? null : stStok.getId(),
                    stDepo == null ? null : stDepo.getId(),
                    miktar, aciklama);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            refreshUretimler();
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

    public void completeProduction() {
        if (seciliTalep == null || seciliTalep.getId() == null) {
            FacesUtil.addErrorMessage("Üretim için talep seçilmelidir.");
            return;
        }
        try {
            workflowService.completeProduction(seciliTalep.getId(), aciklama);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            refreshUretimler();
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

    private void refreshUretimler() {
        uretimler = seciliTalep == null ? Collections.emptyList() : service.findByTalep(seciliTalep);
    }
}
