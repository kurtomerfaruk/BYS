package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EySosyalIncelemeRaporu;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 29.06.2025 11:42
 */
@Named
@ViewScoped
@Slf4j
public class EySosyalIncelemeRaporuController extends AbstractController<EySosyalIncelemeRaporu> {

    @Serial
    private static final long serialVersionUID = 8193041057115591977L;

    @Inject
    private FilterOptionService filterOptionService;

    public EySosyalIncelemeRaporuController() {
        super(EySosyalIncelemeRaporu.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EYDEGERLENDIRME_DURUMU -> {
                return filterOptionService.getEyDegerlendirmeDurumus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public EySosyalIncelemeRaporu prepareCreate(ActionEvent event) {
        EySosyalIncelemeRaporu newItem;
        try {
            newItem = EySosyalIncelemeRaporu.class.getDeclaredConstructor().newInstance();
            newItem.setRaporTarihi(LocalDateTime.now());
            newItem.setRaporuDuzenleyenGnlPersonel(this.getSyKullanici().getGnlPersonel());
            EyKisi kisi = EyKisi.builder().gnlKisi(new GnlKisi()).build();
            newItem.setEyKisi(kisi);
            this.setSelected(newItem);

            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        EyKisi eyKisi = event.getObject();
        this.getSelected().setEyKisi(eyKisi);
    }
}
