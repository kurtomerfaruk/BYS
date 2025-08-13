package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

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
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShObeziteAnket;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.07.2025 12:19
 */
@Named
@ViewScoped
@Slf4j
public class ShObeziteAnketController extends AbstractController<ShObeziteAnket> {

    @Serial
    private static final long serialVersionUID = -8416777941099089753L;

    @Inject
    private FilterOptionService filterOptionService;

    public ShObeziteAnketController() {
        super(ShObeziteAnket.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public ShObeziteAnket prepareCreate(ActionEvent event) {
        ShObeziteAnket newItem;
        try {
            newItem = ShObeziteAnket.class.getDeclaredConstructor().newInstance();
            newItem.setShKisi(ShKisi.builder().gnlKisi(new GnlKisi()).build());
            newItem.setAnketTarihi(LocalDateTime.now());
            newItem.setBoy(BigDecimal.ZERO);
            newItem.setKilo(BigDecimal.ZERO);
            newItem.setAnketiYapanGnlPersonel(this.getSyKullanici().getGnlPersonel());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenShKisi(SelectEvent<ShKisi> event) {
        ShKisi shKisi = event.getObject();
        this.getSelected().setShKisi(shKisi);
    }

    public void calculateBki() {
        double boy = this.getSelected().getBoy().doubleValue();
        double kilo = this.getSelected().getKilo().doubleValue();

        if (boy > 0) {
            boy = boy / 100;
        }

        double bki = kilo / (Math.pow(boy, 2));
        this.getSelected().setBki(new BigDecimal(bki));

        String sonuc = "";
        if (bki < 18.5) {
            sonuc = "Zayıflık";
        } else if (bki >= 18.5 && bki <= 24.9) {
            sonuc = "Normal";
        } else if (bki >= 25 && bki <= 29.9) {
            sonuc = "Hafif Şişman";
        } else if (bki >= 30 && bki <= 39.9) {
            sonuc = "Şişman";
        } else {
            sonuc = "Aşırı Düzeyde Şişman";
        }
        this.getSelected().setKatilimSonucu(sonuc);
    }
}
