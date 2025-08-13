package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyAracTamiriYapilanIslem;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyArizaTuru;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlTalepDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.EnumUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracTamir;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 27.06.2025 13:45
 */
@Setter
@Getter
@Named
@ViewScoped
@Slf4j
public class EyAracTamirController extends AbstractController<EyAracTamir> {

    @Serial
    private static final long serialVersionUID = -6420502565753069651L;

    @Inject
    private FilterOptionService filterOptionService;

    private List<EnumEyAracTamiriYapilanIslem> yapilanIslems;
    private List<EnumEyArizaTuru> arizaTurus;

    public EyAracTamirController() {
        super(EyAracTamir.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case EYARAC_BILGISI -> {
                return filterOptionService.getEyAracBilgisis();
            }
            case EYARAC_ARIZA_TURU -> {
                return filterOptionService.getEyAracArizaTurus();
            }
            case EVET_HAYIR -> {
                return filterOptionService.getEvetHayirs();
            }
            case TALEP_DURUMU -> {
                return filterOptionService.getTalepDurumus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public EyAracTamir prepareCreate(ActionEvent event) {
        EyAracTamir newItem;
        try {
            newItem = EyAracTamir.class.getDeclaredConstructor().newInstance();
            EyKisi eyKisi = new EyKisi();
            eyKisi.setGnlKisi(GnlKisi.builder().build());
            newItem.setEyKisi(eyKisi);
            newItem.setKayitTarihi(LocalDateTime.now());
            newItem.setTamirOncesiTeslimAlanGnlPersonel(new GnlPersonel());
            newItem.setDurum(EnumGnlTalepDurumu.BEKLIYOR);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            if (this.getSelected().getTeslimTarihi() == null) {
                this.getSelected().setTeslimTarihi(LocalDateTime.now());
            }
            yapilanIslems = new ArrayList<>();
            String yapilanIslem = this.getSelected().getYapilanIslem();
            if (StringUtil.isNotBlank(yapilanIslem)) {
                String stringWithNoBrackets = StringUtil.removeBracket(yapilanIslem);
                yapilanIslems = Arrays.stream(stringWithNoBrackets.split(","))
                        .map(String::trim)
                        .map(EnumEyAracTamiriYapilanIslem::valueOf)
                        .collect(Collectors.toList());
            }
            arizaTurus = new ArrayList<>();
            String arizaTuru = this.getSelected().getArizaTuru();
            if (StringUtil.isNotBlank(arizaTuru)) {
                arizaTurus = this.getSelected().getArizaTurus();
            }
        }
    }

    public void create(ActionEvent event) {
        if (this.getSelected() != null) {
            generateArizaTuru();
            this.saveNew(event);
        }
    }

    public void update(ActionEvent event) {
        if (this.getSelected() != null) {
            String yapilanIslemValues = EnumUtil.enumListToString(yapilanIslems);
            this.getSelected().setYapilanIslem(yapilanIslemValues);
            generateArizaTuru();
            this.save(event);
        }
    }

    private void generateArizaTuru() {
        String arizaTurleri = EnumUtil.enumListToString(arizaTurus);
        this.getSelected().setArizaTuru(arizaTurleri);
    }

    public void secilenEyKisi(SelectEvent<EyKisi> event) {
        EyKisi eyKisi = event.getObject();
        this.getSelected().setEyKisi(eyKisi);
    }
}
