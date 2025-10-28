package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbDurum;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbHizmetPlanlama;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbTalep;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbVerilecekHizmet;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service.EdbVerilecekHizmetService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 9.07.2025 08:41
 */
@Named
@ViewScoped
@Slf4j
public class EdbHizmetPlanlamaController extends AbstractController<EdbHizmetPlanlama> {

    @Serial
    private static final long serialVersionUID = 5659970830593991395L;

    @Inject
    private EdbVerilecekHizmetService verilecekHizmetService;
    @Inject
    private FilterOptionService filterOptionService;

    public EdbHizmetPlanlamaController() {
        super(EdbHizmetPlanlama.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    public EdbHizmetPlanlama prepareCreate(ActionEvent event) {
        EdbHizmetPlanlama newItem;
        try {
            newItem = EdbHizmetPlanlama.class.getDeclaredConstructor().newInstance();
            newItem.setBaslangicTarihi(LocalDateTime.now());
            EdbTalep edbTalep = EdbTalep.builder()
                    .edbBasvuru(EdbBasvuru.builder().gnlKisi(new GnlKisi()).build())
                    .build();
            newItem.setEdbTalep(edbTalep);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null);
        }
        return null;
    }

    public void secilenTalep(SelectEvent<EdbTalep> event) {
        EdbTalep talep = event.getObject();
        this.getSelected().setEdbTalep(talep);
    }

    public void addService(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                EdbVerilecekHizmet hizmetler = verilecekHizmetService.findByHizmetPlanlamaId(this.getSelected());
                if (hizmetler == null) {
                    hizmetler = new EdbVerilecekHizmet();
                    hizmetler.setEdbHizmetPlanlama(this.getSelected());
                    hizmetler.setTarih(this.getSelected().getBaslangicTarihi().toLocalDate());
                    hizmetler.setDurum(EnumEdbDurum.BEKLIYOR);
                    verilecekHizmetService.create(hizmetler);
                    this.getSelected().setHizmetlereEklendi(true);
                    save(event);
                    FacesUtil.successMessage("hizmetEklendi");
                }
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}
