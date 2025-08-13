package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyYetki;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:35
 */
@Named
@ViewScoped
@Slf4j
public class SyYetkiController extends AbstractController<SyYetki> {

    @Serial
    private static final long serialVersionUID = 6778479691782374357L;

    public SyYetkiController() {
        super(SyYetki.class);
    }

    public void saveProceed(ActionEvent event) {
        SyYetki oldYetki = this.getSelected();
        saveNew(event);
        SyYetki yetki = prepareCreate(event);
        String yetkiTanim = oldYetki.getYetki().split(":")[0];
        yetki.setYetki(yetkiTanim+":");
        yetki.setAciklama(StringUtil.removeLastPart(oldYetki.getAciklama()));
        this.setSelected(yetki);
    }
}
