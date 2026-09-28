package tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.psikiyatriklinigi.entity.*;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.21.0
 * @since 28.09.2026 08:28
 */
@Named
@ViewScoped
@Slf4j
public class PkAileController extends AbstractController<PkAile> {

    @Serial
    private static final long serialVersionUID = -4271019910855765181L;

    @Inject
    private KpsController kpsController;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private FilterOptionService filterOptionService;

    public PkAileController() {
        super(PkAile.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case YAKINLIK_DERECESI -> {
                return filterOptionService.getGnlYakinlikDerecesis();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public PkAile prepareCreate(ActionEvent event) {
        PkAile newItem;
        try {
            newItem = PkAile.class.getDeclaredConstructor().newInstance();
            newItem.setBasvuruTarihi(LocalDateTime.now());
            newItem.setPkHasta(new PkHasta());
            GnlKisi gnlKisi = GnlKisi.builder().gnlIlce(new GnlIlce()).gnlMahalle(new GnlMahalle()).build();
            newItem.setGnlKisi(gnlKisi);
            newItem.setPkHastaCocuklukDonemi(new PkHastaCocuklukDonemi());
            newItem.setPkHastaAileIciIliski(new PkHastaAileIciIliski());
            newItem.setPkHastaAdliSicil(new PkHastaAdliSicil());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenPkHasta(SelectEvent<PkHasta> event) {
        PkHasta pkHasta = event.getObject();
        this.getSelected().setPkHasta(pkHasta);
    }

    public void getTcKimlik() {
        if (this.getSelected() == null) return;
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
