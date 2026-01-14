package tr.bel.gaziantep.bysweb.moduls.hafriyat.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.entity.*;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.service.HfAracService;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.service.HfHafriyatIsFaturaService;
import tr.bel.gaziantep.bysweb.moduls.hafriyat.service.HfHafriyatIsService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 31.07.2025 08:22
 */
@Named
@ViewScoped
@Slf4j
public class HfHafriyatIsController extends AbstractController<HfHafriyatIs> {

    @Serial
    private static final long serialVersionUID = -1803836982896964928L;

    @Inject
    private HfHafriyatIsService service;
    @Inject
    private HfAracService hfAracService;
    @Inject
    private HfHafriyatIsFaturaService hfHafriyatIsFaturaService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<HfArac> selectedAracs;
    @Getter
    @Setter
    private HfArac arac;
    @Getter
    @Setter
    private HfHafriyatIsFatura fatura;

    public HfHafriyatIsController() {
        super(HfHafriyatIs.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case BELEDIYE -> {
                return filterOptionService.getGnlBelediyes();
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
    public HfHafriyatIs prepareCreate(ActionEvent event) {
        HfHafriyatIs newItem;
        try {
            newItem = HfHafriyatIs.class.getDeclaredConstructor().newInstance();
            selectedAracs = new ArrayList<>();
            newItem.setUreticiHfFirma(new HfFirma());
            newItem.setTasiyiciHfFirma(new HfFirma());
            newItem.setDepolamaTesisiHfFirma(new HfFirma());
            newItem.setTasimaAracMiktari(0);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }


    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                List<HfHafriyatIsArac> aracList = convertAracToHafriyatIsArac();
                if (!aracList.isEmpty()) {
                    this.getSelected().setHfHafriyatIsAracList(aracList);
                }
                if (this.getSelected().getDepolamaTesisiHfFirma().getId() == null) {
                    this.getSelected().setDepolamaTesisiHfFirma(null);
                }
                service.create(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                List<HfHafriyatIsArac> aracList = convertAracToHafriyatIsArac();
                if (!aracList.isEmpty()) {
                    this.getSelected().setHfHafriyatIsAracList(aracList);
                }
                if (this.getSelected().getDepolamaTesisiHfFirma().getId() == null) {
                    this.getSelected().setDepolamaTesisiHfFirma(null);
                }
                service.edit(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    private List<HfHafriyatIsArac> convertAracToHafriyatIsArac() {
        return selectedAracs
                .stream()
                .map(
                        selectedArac -> HfHafriyatIsArac.builder()
                                .hfArac(selectedArac)
                                .hfFirma(this.getSelected().getUreticiHfFirma())
                                .hfHafriyatIs(this.getSelected())
                                .build())
                .collect(Collectors.toList());
    }

    public void secilenUreticiFirma(SelectEvent<HfFirma> event) {
        HfFirma firma = event.getObject();
        this.getSelected().setUreticiHfFirma(firma);
    }

    public void secilenTasiyiciFirma(SelectEvent<HfFirma> event) {
        HfFirma firma = event.getObject();
        this.getSelected().setTasiyiciHfFirma(firma);
    }

    public void secilenDepolamaTesisiFirma(SelectEvent<HfFirma> event) {
        HfFirma firma = event.getObject();
        this.getSelected().setDepolamaTesisiHfFirma(firma);
    }

    public List<HfArac> completeArac(String query) {
        String queryLowerCase = query.toLowerCase();
        List<HfArac> aracs = hfAracService.findAll();
        return aracs.stream().filter(t -> t.getPlaka().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
    }

    public void calculate() {

        var selected = this.getSelected();

        BigDecimal tasimaUcreti = BigDecimal.ZERO;
        BigDecimal gercekMiktar = selected.getGercekMiktar();
        BigDecimal tahminiMiktar = selected.getTahminiMiktar();
        BigDecimal atikFiyat = selected.getAtikCinsiFiyat();

        BigDecimal tasimaFiyat = selected.getTasimaUcreti() != null ? selected.getTasimaUcreti() : BigDecimal.ZERO;
        BigDecimal tasimaMiktar = BigDecimal.valueOf(selected.getTasimaAracMiktari());
        tasimaUcreti = tasimaFiyat.multiply(tasimaMiktar).setScale(2, RoundingMode.CEILING);

        BigDecimal gercekTutar = tutarHesapla(gercekMiktar, atikFiyat);
        BigDecimal tahminiTutar = tutarHesapla(tahminiMiktar, atikFiyat);

        selected.setGercekTutar(gercekTutar.add(tasimaUcreti));
        selected.setTahminiTutar(tahminiTutar.add(tasimaUcreti));
    }

    public BigDecimal tutarHesapla(BigDecimal miktar, BigDecimal fiyat) {
        if (miktar == null || fiyat == null) return BigDecimal.ZERO;

        return fiyat.multiply(miktar).setScale(2, RoundingMode.CEILING);
    }

    public void addCar() {

        selectedAracs.add(arac);
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        arac = new HfArac();
    }

    public void removeCar(HfArac item) {
        if (selectedAracs.contains(item)) {
            selectedAracs.remove(item);
            FacesUtil.successMessage(Constants.KAYIT_SILINDI);
        }
    }

    public void addInvoice() {
        fatura.setHfHafriyatIs(this.getSelected());
        this.getSelected().getHfHafriyatIsFaturaList().add(fatura);
        FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        fatura = new HfHafriyatIsFatura();
    }

    public void removeInvoice(HfHafriyatIsFatura item) {
        try {
            if (this.getSelected().getHfHafriyatIsFaturaList().contains(item)) {
                this.getSelected().getHfHafriyatIsFaturaList().remove(item);
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
                item.setAktif(false);
                hfHafriyatIsFaturaService.edit(item);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfos(ActionEvent event) {
        if (this.getSelected() != null) {
            if (selectedAracs == null) {
                selectedAracs = new ArrayList<>();
            }
            arac = new HfArac();
            if (this.getSelected().getHfHafriyatIsFaturaList() == null) {
                this.getSelected().setHfHafriyatIsFaturaList(new ArrayList<>());
            }
            fatura = new HfHafriyatIsFatura();

            if (this.getSelected().getDepolamaTesisiHfFirma() == null) {
                this.getSelected().setDepolamaTesisiHfFirma(new HfFirma());
            }

            this.getSelected().getHfHafriyatIsAracList().forEach(isArac -> selectedAracs.add(isArac.getHfArac()));
        }
    }

    public void hfHafriyatIsSecKapat(HfHafriyatIs hfHafriyatIs) {
        PrimeFaces.current().dialog().closeDynamic(hfHafriyatIs);
    }

    public void onRowDblSelect(SelectEvent<HfHafriyatIs> event) {
        HfHafriyatIs hfHafriyatIs = event.getObject();
        hfHafriyatIsSecKapat(hfHafriyatIs);
    }

    public void deliverDocument() {
        try {
            if (this.getSelected() != null) {
                if (!this.getSelected().isEvrakTeslimEdildiMi()) {
                    this.getSelected().setEvrakTeslimEdildiMi(true);
                    service.edit(this.getSelected());
                    FacesUtil.successMessage("evrakTeslimEdildi");
                } else {
                    FacesUtil.warningMessage("evrakZatenTeslimEdilmis");
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
