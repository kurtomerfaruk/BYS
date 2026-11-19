package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoYetki;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoService;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoYetkiService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.30.0
 * @since 18.11.2026 09:28
 */
@Named
@ViewScoped
@Slf4j
public class StDepoYetkiController extends AbstractController<StDepoYetki> {

    @Serial
    private static final long serialVersionUID = 3678359720551246826L;

    @Inject
    private StDepoYetkiService service;
    @Inject
    private StDepoService depoService;

    @Getter
    @Setter
    private List<StDepo> selectedDepos;
    private List<StDepo> depoList = new ArrayList<>();

    public StDepoYetkiController() {
        super(StDepoYetki.class);
    }

    @Override
    public StDepoYetki prepareCreate(ActionEvent event) {
        StDepoYetki yeni = super.prepareCreate(event);
        depoList = depoService.findAll();
        selectedDepos = new ArrayList<>();
        if (yeni != null) {
            yeni.setSyKullanici(new SyKullanici());
            yeni.setStDepo(new StDepo());
        }
        return yeni;
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() == null) return;
        if (hasPermission()) return;
        super.save(event);
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() == null) return;
        try {
            service.saveNew(this.getSelected(), selectedDepos);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    private boolean hasPermission() {
        StDepoYetki yetki = service.findByKullaniciByDepo(this.getSelected().getSyKullanici(), this.getSelected().getStDepo());
        if (yetki != null) {
            FacesUtil.addExclamationMessage("Seçmiş olduğunuz kullanıcı ve depo daha önceden eklenmiştir.");
            return true;
        }
        return false;
    }

    public List<StDepo> completeDepos(String query) {
        String queryLowerCase = query.toLowerCase();
        List<StDepo> result = depoList.stream().filter(t -> t.getAd().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        result.removeAll(selectedDepos);
        return result;
    }

    public void onItemUnSelect(UnselectEvent<StDepo> event) {
        StDepo depo = event.getObject();
        selectedDepos.remove(depo);
    }

    public void onItemSelect(SelectEvent<StDepo> event) {
        StDepo depo = event.getObject();
        if (!selectedDepos.contains(depo)) {
            selectedDepos.add(depo);
        }
    }
}
