package tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.controller;

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
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.evimdunyalarabedel.EnumEdbYara;
import tr.bel.gaziantep.bysweb.core.enums.genel.*;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyBirim;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiKullandigiCihaz;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.entity.EdbBasvuru;
import tr.bel.gaziantep.bysweb.moduls.evimdunyalarabedel.service.EdbBasvuruService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisiAldigiYardimlar;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisiFaydalandigiHaklar;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisiYardimAlinanYerler;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 7.07.2025 08:53
 */
@Named
@ViewScoped
@Slf4j
public class EdbBasvuruController extends AbstractController<EdbBasvuru> {

    @Serial
    private static final long serialVersionUID = 4108020229529007277L;

    @Inject
    private EdbBasvuruService service;
    @Inject
    private GnlKisiService kisiService;
    @Inject
    private KpsController kpsController;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private EdbBasvuru basvuruView;
    @Getter
    @Setter
    private List<EnumGnlFaydalandigiHak> faydalandigiHaklars;
    @Getter
    @Setter
    private List<EnumGnlGelirKaynagi> gelirKaynagis;
    @Getter
    @Setter
    private List<EnumGnlYardimAlinanYerler> yardimAlinanYerlerList;
    @Getter
    @Setter
    private List<EnumGnlYardimTuru> aldigiYardimlarList;
    @Getter
    @Setter
    private List<AyBirim> digerBirimlerList;
    @Getter
    @Setter
    private List<EyEngelGrubu> engelGrubuList;
    @Getter
    @Setter
    private List<EnumEyKullandigiCihaz> kullandigiCihazlars;
    @Getter
    @Setter
    private List<EnumEdbYara> yaraList;
    @Getter
    @Setter
    private List<EdbBasvuru> edbBasvuruList;

    public EdbBasvuruController() {
        super(EdbBasvuru.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case EDBBASVURU_DURUMU -> {
                return filterOptionService.getEdbBasvuruDurumus();
            }
            case KISI_DURUM -> {
                return filterOptionService.getGnlKisiDurums();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    public EdbBasvuru prepareCreate(ActionEvent event) {
        EdbBasvuru newItem;
        try {
            createTempObject();
            newItem = EdbBasvuru.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .eklemeYeri(EnumModul.EDB).build();
            newItem.setGnlKisi(kisi);
            newItem.setBasvuruTarihi(LocalDateTime.now());
            newItem.setBasvuruDurumu(EnumEdbBasvuruDurumu.ON_KAYIT);
            newItem.setEyKisi(new EyKisi());
            edbBasvuruList = new ArrayList<>();
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void createTempObject() {
        faydalandigiHaklars = new ArrayList<>();
        gelirKaynagis = new ArrayList<>();
        yardimAlinanYerlerList = new ArrayList<>();
        aldigiYardimlarList = new ArrayList<>();
        digerBirimlerList = new ArrayList<>();
        engelGrubuList = new ArrayList<>();
        kullandigiCihazlars = new ArrayList<>();
    }

    public void getApplicant() {
        if (this.getSelected() != null) {
            if (this.getSelected().isBasvuranKendisi()) {
                this.getSelected().setKisiYakiniAdi(this.getSelected().getGnlKisi().getAd());
                this.getSelected().setKisiYakiniSoyadi(this.getSelected().getGnlKisi().getSoyad());
                this.getSelected().setKisiYakiniTcKimlikNo(this.getSelected().getGnlKisi().getTcKimlikNo());
                this.getSelected().setKisiYakiniTelefon(this.getSelected().getGnlKisi().getTelefon());
                this.getSelected().setKisiYakiniYakinlikDerecesi(EnumGnlYakinlikDerecesi.KENDISI);
                this.getSelected().setKisiYakiniGnlIlce(this.getSelected().getGnlKisi().getGnlIlce());
                this.getSelected().setKisiYakiniAdres(this.getSelected().getGnlKisi().getAdres());
            }
        }
    }

    public void getTcKimlik() {
        if (this.getSelected() != null) {
            try {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();

                GnlKisi kisi = kisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.EDB);
                this.getSelected().setGnlKisi(kisi);
                kisi.getGnlKisiFaydalandigiHaklarList().stream()
                        .filter(GnlKisiFaydalandigiHaklar::isSecili)
                        .map(GnlKisiFaydalandigiHaklar::getTanim)
                        .forEach(faydalandigiHaklars::add);
                kisi.getGnlKisiAldigiYardimlarList().stream()
                        .filter(GnlKisiAldigiYardimlar::isSecili)
                        .map(GnlKisiAldigiYardimlar::getTanim)
                        .forEach(aldigiYardimlarList::add);
                kisi.getGnlKisiYardimAlinanYerlerList().stream()
                        .filter(GnlKisiYardimAlinanYerler::isSecili)
                        .map(GnlKisiYardimAlinanYerler::getTanim)
                        .forEach(yardimAlinanYerlerList::add);
                EyKisi eyKisi = eyKisiService.findByKisi(kisi);
                if (eyKisi != null) {
                    eyKisi.setAktif(true);
                    eyKisi.getEyKisiEngelGrubuList().stream()
                            .filter(EyKisiEngelGrubu::isSecili)
                            .map(EyKisiEngelGrubu::getEyEngelGrubu)
                            .forEach(engelGrubuList::add);
                    eyKisi.getEyKisiKullandigiCihazList().stream()
                            .filter(EyKisiKullandigiCihaz::isSecili)
                            .map(EyKisiKullandigiCihaz::getTanim)
                            .forEach(kullandigiCihazlars::add);
                    this.getSelected().setEngelDurumu(true);
                    this.getSelected().setEyKisi(eyKisi);
                }

                edbBasvuruList = service.findByGnlKisi(kisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void isDisabled() {
        if (this.getSelected() != null) {
            if (this.getSelected().isEngelDurumu()) {
                this.getSelected().setEyKisi(EyKisi.builder().eyKisiEngelGrubuList(new ArrayList<>()).build());
            } else {
                this.getSelected().setEyKisi(null);
            }
        }
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            engelGrubuList = new ArrayList<>();
            readDisableInfo();
            edbBasvuruList = service.findByGnlKisi(this.getSelected().getGnlKisi());
        }
    }

    private void readDisableInfo() {
        if (this.getSelected().getEyKisi() != null) {
            for (EyKisiEngelGrubu eyKisiEngelgrubu : this.getSelected().getEyKisi().getEyKisiEngelGrubuList()) {
                if (eyKisiEngelgrubu.isSecili()) {
                    engelGrubuList.add(eyKisiEngelgrubu.getEyEngelGrubu());
                }
            }
        }
    }

    @Override
    public void saveNew(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                List<EyKisiEngelGrubu> eyKisiEngelgrubus = checkKisiEngelGrubu(this.getSelected().getEyKisi().getEyKisiEngelGrubuList(),
                        this.getSelected().getEyKisi());
                this.getSelected().getEyKisi().setEyKisiEngelGrubuList(eyKisiEngelgrubus);
                service.merge(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                createTempObject();
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
                if (this.getSelected().getEyKisi() != null) {
                    List<EyKisiEngelGrubu> eyKisiEngelgrubus = checkKisiEngelGrubu(this.getSelected().getEyKisi().getEyKisiEngelGrubuList(),
                            this.getSelected().getEyKisi());
                    this.getSelected().getEyKisi().setEyKisiEngelGrubuList(eyKisiEngelgrubus);
                }

                service.merge(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                createTempObject();
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public List<EyKisiEngelGrubu> checkKisiEngelGrubu(List<EyKisiEngelGrubu> eyKisiEngelgrubus, EyKisi eyKisi) {
        if (eyKisiEngelgrubus != null) {
            eyKisiEngelgrubus.forEach(eyKisiEngelgrubu -> eyKisiEngelgrubu.setSecili(false));
        }

        for (EyEngelGrubu grubus : engelGrubuList) {
            assert eyKisiEngelgrubus != null;
            EyKisiEngelGrubu keg = eyKisiEngelgrubus
                    .stream()
                    .filter(x -> x.getEyKisi().getId().equals(eyKisi.getId()) && x.getEyEngelGrubu().getId().equals(grubus.getId()))
                    .findFirst()
                    .orElse(null);
            if (keg == null) {
                keg = new EyKisiEngelGrubu();
                keg.setEyKisi(eyKisi);
                keg.setEyEngelGrubu(grubus);
            }
            keg.setSecili(true);
            if (!eyKisiEngelgrubus.contains(keg)) {
                eyKisiEngelgrubus.add(keg);
            }
        }
        return eyKisiEngelgrubus;
    }

    public void onRowDblSelect(SelectEvent<EdbBasvuru> event) {
        EdbBasvuru edbBasvuru = event.getObject();
        edbBasvuruSecKapat(edbBasvuru);
    }

    public void edbBasvuruSecKapat(EdbBasvuru edbBasvuru) {
        PrimeFaces.current().dialog().closeDynamic(edbBasvuru);
    }


    public void openBasvuruView(EdbBasvuru basvuru) {
        if (basvuru != null) {
            basvuruView = basvuru;
        }
    }

}
