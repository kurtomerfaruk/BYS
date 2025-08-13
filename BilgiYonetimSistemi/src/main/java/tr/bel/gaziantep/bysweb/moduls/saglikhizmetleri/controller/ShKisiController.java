package tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.controller;

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
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisi;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKontrol;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiKontrolService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 11.07.2025 16:20
 */
@Named
@ViewScoped
@Slf4j
public class ShKisiController extends AbstractController<ShKisi> {

    @Serial
    private static final long serialVersionUID = 4775470442986056899L;

    @Inject
    private ShKisiService service;
    @Inject
    private GnlKisiService kisiService;
    @Inject
    private ShKisiKontrolService kisiKontrolService;
    @Inject
    private KpsController kpsController;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private ShKisiKontrol shKisiKontrol;

    public ShKisiController() {
        super(ShKisi.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case CINSIYET -> {
                return filterOptionService.getGnlCinsiyets();
            }
            case MEDENI_DURUM -> {
                return filterOptionService.getGnlMedeniDurums();
            }
            case KISI_DURUM -> {
                return filterOptionService.getGnlKisiDurums();
            }
            case SHDANISMANLIK_HIZMETI -> {
                return filterOptionService.getShDanismanlikHizmetis();
            }
            case SHOBEZITE_HIZMETI -> {
                return filterOptionService.getShObeziteHizmets();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    public ShKisi prepareCreate(ActionEvent event) {
        ShKisi newItem;
        try {
            newItem = ShKisi.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .eklemeYeri(EnumModul.SAGLIK_HIZMETLERI)
                    .build();
            newItem.setGnlKisi(kisi);
            newItem.setGelisTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
        if (this.getSelected() != null) {
            try {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();

                GnlKisi kisi = kisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.SAGLIK_HIZMETLERI);
                this.getSelected().setGnlKisi(kisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void addControl() {
        if (shKisiKontrol != null) {
            if (this.getSelected().getShKisiKontrolList() == null) {
                this.getSelected().setShKisiKontrolList(new ArrayList<>());
            }
            this.getSelected().getShKisiKontrolList().add(shKisiKontrol);
            shKisiKontrol = new ShKisiKontrol();
        }
    }

    public void removeControl(ShKisiKontrol kontrol) {
        if (kontrol != null) {
            try {
                this.getSelected().getShKisiKontrolList().remove(kontrol);
                kontrol.setAktif(false);
                kisiKontrolService.edit(kontrol);
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void getBki() {
        try {
            if (shKisiKontrol.getKilo() != null && shKisiKontrol.getBoy() != null) {
                double boy = shKisiKontrol.getBoy().doubleValue();
                double kilo = shKisiKontrol.getKilo().doubleValue();

                if (boy > 0) {
                    boy = boy / 100;
                }

                double bki = kilo / (Math.pow(boy, 2));
                shKisiKontrol.setBki(new BigDecimal(bki).setScale(2, RoundingMode.HALF_UP));
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            shKisiKontrol = ShKisiKontrol.builder().shKisi(this.getSelected()).build();
        }
    }

    public void nviUpdate(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                if (this.getSelected().getGnlKisi().getDogumTarihi() == null) {
                    FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected().getGnlKisi(), EnumModul.SAGLIK_HIZMETLERI);
                kisiService.edit(kisi);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void saveNew(ActionEvent event) {
        try {
            boolean existGnlKisi = service.existByGnlKisi(this.getSelected().getGnlKisi());
            if(existGnlKisi){
                FacesUtil.warningMessage("mukerrerKisiKayit");
                return;
            }
            service.create(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void save(ActionEvent event) {
        try {
            boolean exist = service.existByGnlKisiByShKisi(this.getSelected());
            if(exist){
                FacesUtil.warningMessage("mukerrerKisiKayit");
                return;
            }
            service.edit(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void shKisiSecKapat(ShKisi shKisi) {
        PrimeFaces.current().dialog().closeDynamic(shKisi);
    }

    public void onRowDblSelect(SelectEvent<ShKisi> event) {
        ShKisi shKisi = event.getObject();
        shKisiSecKapat(shKisi);
    }

}
