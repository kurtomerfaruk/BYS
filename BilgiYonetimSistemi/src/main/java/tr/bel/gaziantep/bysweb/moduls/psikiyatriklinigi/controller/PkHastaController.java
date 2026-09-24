package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkHasta;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.PkMadde;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 22.09.2026 14:20
 */
@Named
@ViewScoped
@Slf4j
public class PkHastaController extends AbstractController<PkHasta> {

    @Serial
    private static final long serialVersionUID = -5396860367374859970L;

    @Inject
    private KpsController kpsController;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<PkMadde> maddeList;
    @Getter
    @Setter
    private List<String> maddeKullananBireyler ;
    @Getter
    @Setter
    private List<String> alkolKullananBireyler ;

    public PkHastaController() {
        super(PkHasta.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case KISI_DURUM -> {
                return filterOptionService.getGnlKisiDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public PkHasta prepareCreate(ActionEvent event) {
        PkHasta newItem;
        try {
            newItem = PkHasta.class.getDeclaredConstructor().newInstance();
            newItem.setGnlKisi(new GnlKisi());
            newItem.setBasvuruTarihi(LocalDateTime.now());
            maddeList=new ArrayList<>();
            maddeKullananBireyler = new ArrayList<>();
            alkolKullananBireyler = new ArrayList<>();
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
        if (this.getSelected() != null) {
            try {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();

                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.PSIKIYATRI_KLINIGI);
                this.getSelected().setGnlKisi(kisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }
}
