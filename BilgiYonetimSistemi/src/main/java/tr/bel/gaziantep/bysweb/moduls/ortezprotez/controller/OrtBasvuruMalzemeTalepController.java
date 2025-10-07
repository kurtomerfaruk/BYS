package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.10.2025 10:03
 */
@Named
@ViewScoped
@Slf4j
public class OrtBasvuruMalzemeTalepController extends AbstractController<OrtBasvuruMalzemeTalep> {

    @Serial
    private static final long serialVersionUID = 7783411043036481695L;

    @Inject
    private OrtPersonelService personelService;

    @Getter
    @Setter
    private OrtPersonel ortPersonel;

    public OrtBasvuruMalzemeTalepController() {
        super(OrtBasvuruMalzemeTalep.class);
    }

    @Override
    @PostConstruct
    public void init() {
        super.init();
        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
        if (ortPersonel == null) {
            FacesUtil.addErrorMessage("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
        }
    }

    @Override
    public OrtBasvuruMalzemeTalep prepareCreate(ActionEvent event) {
        OrtBasvuruMalzemeTalep newItem;
        try {
            newItem = OrtBasvuruMalzemeTalep.class.getDeclaredConstructor().newInstance();
            OrtHasta ortHasta = OrtHasta.builder().gnlKisi(new GnlKisi()).build();
            newItem.setOrtBasvuru(OrtBasvuru.builder().ortHasta(ortHasta).build());
            newItem.setOrtStok(new OrtStok());
            newItem.setTalepTarihi(LocalDateTime.now());
            newItem.setDurum(EnumOrtMalzemeOnayDurumu.BEKLEMEDE);
            newItem.setTalepEdenOrtPersonel(ortPersonel);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenOrtBasvuru(SelectEvent<OrtBasvuru> event) {
        OrtBasvuru ortBasvuru = event.getObject();
        this.getSelected().setOrtBasvuru(ortBasvuru);
    }
    public void secilenOrtStok(SelectEvent<OrtStok> event) {
        OrtStok ortStok = event.getObject();
        this.getSelected().setOrtStok(ortStok);
    }
}
