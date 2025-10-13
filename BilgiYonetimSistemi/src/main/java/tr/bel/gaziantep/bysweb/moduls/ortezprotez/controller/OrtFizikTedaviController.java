package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtFizikTedaviDurum;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtFizikTedavi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 8.10.2025 15:31
 */
@Named
@ViewScoped
@Slf4j
public class OrtFizikTedaviController extends AbstractController<OrtFizikTedavi> {

    @Serial
    private static final long serialVersionUID = 4739534507739056360L;

    @Inject
    private OrtPersonelService personelService;

    @Getter
    @Setter
    private OrtPersonel ortPersonel;

    public OrtFizikTedaviController() {
        super(OrtFizikTedavi.class);
    }

    @Override
    public OrtFizikTedavi prepareCreate(ActionEvent event) {
        OrtFizikTedavi newItem;
        try {
            newItem = OrtFizikTedavi.class.getDeclaredConstructor().newInstance();
            newItem.setOrtHasta(new OrtHasta());
            newItem.setTarih(LocalDateTime.now());
            newItem.setDurum(EnumOrtFizikTedaviDurum.IPTAL_EDILDI);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void onPreRenderView() {
        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
        if (ortPersonel == null) {
            throw new BysBusinessException("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
        }
    }

    public void secilenOrtHasta(SelectEvent<OrtHasta> event) {
        OrtHasta ortHasta = event.getObject();
        this.getSelected().setOrtHasta(ortHasta);
    }
}
