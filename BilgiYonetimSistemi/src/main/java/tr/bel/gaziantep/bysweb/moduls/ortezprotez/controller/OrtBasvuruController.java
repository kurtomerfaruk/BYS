package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.annotation.PostConstruct;
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
import tr.bel.gaziantep.bysweb.core.enums.ErrorType;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtBasvuru;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtPersonel;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtBasvuruService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtPersonelService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.09.2025 13:48
 */
@Named
@ViewScoped
@Slf4j
public class OrtBasvuruController extends AbstractController<OrtBasvuru> {

    @Serial
    private static final long serialVersionUID = 8798606249246291031L;

    @Inject
    private OrtBasvuruService ortBasvuruService;
    @Inject
    private OrtPersonelService personelService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private OrtPersonel ortPersonel;
    @Getter
    @Setter
    private boolean addAppointment;
    @Getter
    @Setter
    private LocalDateTime appointmentDate;

    public OrtBasvuruController() {
        super(OrtBasvuru.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTBASVURU_DURUMU -> {
                return filterOptionService.getOrtBasvuruDurums();
            }
            case ORTBASVURU_HAREKET_DURUMU -> {
                return filterOptionService.getOrtBasvuruHareketDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }

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
    public OrtBasvuru prepareCreate(ActionEvent event) {
        OrtBasvuru newItem;
        try {
            newItem = OrtBasvuru.class.getDeclaredConstructor().newInstance();
            newItem.setBasvuruTarihi(LocalDateTime.now());
            newItem.setOrtHasta(OrtHasta.builder().gnlKisi(new GnlKisi()).build());
            newItem.setMuayeneYapanOrtPersonel(ortPersonel);
            newItem.setBasvuruDurumu(EnumOrtBasvuruDurumu.BEKLEMEDE);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void secilenOrtHasta(SelectEvent<OrtHasta> event) {
        OrtHasta ortHasta = event.getObject();
        this.getSelected().setOrtHasta(ortHasta);
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected().getOrtHasta() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            ortBasvuruService.save(this.getSelected(), addAppointment, appointmentDate);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void getInfo() {
        if (this.getSelected().getOrtHasta() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        this.getSelected().setRaporuOnaylayanOrtPersonel(ortPersonel);
    }

    public void paid() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        try {
            this.getSelected().setOdendi(StringUtil.isNotBlank(this.getSelected().getMakbuzNo()));
            ortBasvuruService.saveDurum(this.getSelected(), EnumOrtBasvuruHareketDurumu.ODEME_ALINDI);
            FacesUtil.successMessage("odemeAlindi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void saveSutKodu() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        try {
            if (StringUtil.isNotBlank(this.getSelected().getSutKodu())) {
                ortBasvuruService.saveDurum(this.getSelected(), EnumOrtBasvuruHareketDurumu.SUT_KODU_VERILDI);
                FacesUtil.successMessage("sutKoduVerildi");
            }

        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public boolean checkSutKodu() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

//        boolean result = false;
//
//        if (this.getSelected().isUcretli()) {
//            if (this.getSelected().isOdendi()) {
//                result = true;
//            }
//        } else {
//            result = true;
//        }
//
//        return result;
        return !getSelected().isUcretli() || getSelected().isOdendi();
    }
}
