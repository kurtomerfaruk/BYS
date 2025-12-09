package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

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
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyAnketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlFaydalandigiHak;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGelirKaynagi;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimAlinanYerler;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYardimTuru;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyer;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmKursiyerKurs;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmKursiyerService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.*;
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
 * @since 8.12.2025 08:35
 */
@Named
@ViewScoped
@Slf4j
public class EkmKursiyerController extends AbstractController<EkmKursiyer> {

    @Serial
    private static final long serialVersionUID = -8652937008506546011L;

    @Inject
    private EkmKursiyerService service;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kpsController;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<EyEngelGrubu> engelGrubus;
    @Getter
    @Setter
    private List<EnumGnlFaydalandigiHak> faydalandigiHaklars;
    @Getter
    @Setter
    private List<EnumEyMaddeKullanimi> maddeKullanimis;
    @Getter
    @Setter
    private List<EnumGnlGelirKaynagi> aileninGelirKaynagis;
    @Getter
    @Setter
    private List<EnumGnlYardimAlinanYerler> yardimAlinanYerlers;
    @Getter
    @Setter
    private List<EnumGnlYardimTuru> yardimTurus;
    @Getter
    @Setter
    private List<EnumEyKullandigiCihaz> kullandigiCihazs;
    @Getter
    @Setter
    private List<GnlKurs> gnlKursList;

    public EkmKursiyerController() {
        super(EkmKursiyer.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case GNLDEVAM_DURUMU -> {
                return filterOptionService.getGnlDevamDurumus();
            }
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    public EkmKursiyer prepareCreate(ActionEvent event) {
        EkmKursiyer newItem;
        try {
            newItem = EkmKursiyer.class.getDeclaredConstructor().newInstance();
            EyKisi eykisi = new EyKisi();
            eykisi.setAnketDurumu(EnumEyAnketDurumu.ANKET_YAPILMADI);
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .sosyalYardimAliyorMu(false)
                    .eklemeYeri(EnumModul.EKM)
                    .gnlIl(new GnlIl())
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .build();
            eykisi.setGnlKisi(kisi);
            eykisi.setIrtibatKuranGnlPersonel(this.getSyKullanici().getGnlPersonel());
            clearList();
            newItem.setEyKisi(eykisi);
            newItem.setKayitTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void clearList() {
        engelGrubus = new ArrayList<>();
        maddeKullanimis = new ArrayList<>();
        aileninGelirKaynagis = new ArrayList<>();
        yardimAlinanYerlers = new ArrayList<>();
        yardimTurus = new ArrayList<>();
        kullandigiCihazs = new ArrayList<>();
        gnlKursList=new ArrayList<>();
    }

    public void update(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                EyKisi existing = eyKisiService.findByTcKimlikNo(this.getSelected().getEyKisi().getGnlKisi().getTcKimlikNo());

                if (existing != null && !existing.getId().equals(this.getSelected().getEyKisi().getId())) {
                    FacesUtil.addErrorMessage("Bu kişiye ait zaten Engelli kaydı var.");
                    return;
                }

                service.update(this.getSelected(),
                        engelGrubus,
                        faydalandigiHaklars,
                        maddeKullanimis,
                        aileninGelirKaynagis,
                        yardimAlinanYerlers,
                        yardimTurus,
                        kullandigiCihazs,
                        gnlKursList);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.KAYIT_EKLENIRKEN_GUNCELLENIRKEN_HATA_OLUSTU);
        }
    }

    public void getTcKimlik() {
        try {
            if (this.getSelected() != null) {
                String tcKimlikNo = this.getSelected().getEyKisi().getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getEyKisi().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.EKM);
                if (kisi != null) {
                    this.getSelected().getEyKisi().setGnlKisi(kisi);
                }

            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }

    public void readInfo() {
        if (this.getSelected() != null) {
            engelGrubus = Function.filterAndCollect(this.getSelected().getEyKisi().getEyKisiEngelGrubuList(), EyKisiEngelGrubu::isSecili,
                    EyKisiEngelGrubu::getEyEngelGrubu);
            faydalandigiHaklars = Function.filterAndCollect(this.getSelected().getEyKisi().getGnlKisi().getGnlKisiFaydalandigiHaklarList(),
                    GnlKisiFaydalandigiHaklar::isSecili,
                    GnlKisiFaydalandigiHaklar::getTanim);
            maddeKullanimis = Function.filterAndCollect(this.getSelected().getEyKisi().getEyKisiMaddeKullanimiList(), EyKisiMaddeKullanimi::isSecili,
                    EyKisiMaddeKullanimi::getTanim);
            aileninGelirKaynagis = Function.filterAndCollect(this.getSelected().getEyKisi().getGnlKisi().getGnlKisiGelirKaynagiList(), GnlKisiGelirKaynagi::isSecili,
                    GnlKisiGelirKaynagi::getTanim);
            yardimAlinanYerlers = Function.filterAndCollect(this.getSelected().getEyKisi().getGnlKisi().getGnlKisiYardimAlinanYerlerList(),
                    GnlKisiYardimAlinanYerler::isSecili, GnlKisiYardimAlinanYerler::getTanim);
            yardimTurus = Function.filterAndCollect(this.getSelected().getEyKisi().getGnlKisi().getGnlKisiAldigiYardimlarList(), GnlKisiAldigiYardimlar::isSecili,
                    GnlKisiAldigiYardimlar::getTanim);
            kullandigiCihazs = Function.filterAndCollect(this.getSelected().getEyKisi().getEyKisiKullandigiCihazList(), EyKisiKullandigiCihaz::isSecili,
                    EyKisiKullandigiCihaz::getTanim);
            gnlKursList = Function.filterAndCollect(this.getSelected().getEkmKursiyerKursList(), EkmKursiyerKurs::isSecili,EkmKursiyerKurs::getGnlKurs);

            if (this.getSelected().getEyKisi().getIrtibatKuranGnlPersonel() == null) {
                this.getSelected().getEyKisi().setIrtibatKuranGnlPersonel(this.getSyKullanici().getGnlPersonel());
            }
        }
    }

    public void ekmKursiyerSecKapat(EkmKursiyer ekmKursiyer) {
        PrimeFaces.current().dialog().closeDynamic(ekmKursiyer);
    }

    public void onRowDblSelect(SelectEvent<EkmKursiyer> event) {
        EkmKursiyer ekmKursiyer = event.getObject();
        ekmKursiyerSecKapat(ekmKursiyer);
    }
}
