package tr.bel.gaziantep.bysweb.moduls.stokyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepo;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.entity.StDepoYetki;
import tr.bel.gaziantep.bysweb.moduls.stokyonetimi.service.StDepoYetkiService;

import java.io.Serial;

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

    public StDepoYetkiController() {
        super(StDepoYetki.class);
    }

    @Override
    public StDepoYetki prepareCreate(ActionEvent event) {
        StDepoYetki yeni = super.prepareCreate(event);
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
        if (hasPermission()) return;
        super.saveNew(event);
    }

    private boolean hasPermission() {
        StDepoYetki yetki = service.findByKullaniciByDepo(this.getSelected().getSyKullanici(), this.getSelected().getStDepo());
        if (yetki != null) {
            FacesUtil.addExclamationMessage("Seçmiş olduğunuz kullanıcı ve depo daha önceden eklenmiştir.");
            return true;
        }
        return false;
    }
}
