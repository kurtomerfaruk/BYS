package tr.bel.gaziantep.bysweb.moduls.moralevi.controller;

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
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.*;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.*;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeKisiService;

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
 * @since 10.07.2025 14:41
 */
@Named
@ViewScoped
@Slf4j
public class MeKisiController extends AbstractController<MeKisi> {
    @Serial
    private static final long serialVersionUID = 6411063751465379448L;

    @Inject
    private MeKisiService service;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private GnlKisiService kisiService;
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

    public MeKisiController() {
        super(MeKisi.class);
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
    public MeKisi prepareCreate(ActionEvent event) {
        MeKisi newItem;
        try {
            newItem = MeKisi.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .sosyalYardimAliyorMu(false)
                    .eklemeYeri(EnumModul.MORAL_EVI)
                    .uyruk(EnumGnlUyruk.TC)
                    .build();
            EyKisi eyKisi = EyKisi.builder()
                    .gnlKisi(kisi)
                    .eyKisiEngelGrubuList(new ArrayList<>())
                    .build();
            newItem.setEyKisi(eyKisi);
            newItem.setKayitTarihi(LocalDateTime.now());
            clearList();
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void clearList() {
        engelGrubus = new ArrayList<>();
        faydalandigiHaklars = new ArrayList<>();
        maddeKullanimis = new ArrayList<>();
        aileninGelirKaynagis = new ArrayList<>();
        yardimAlinanYerlers = new ArrayList<>();
        yardimTurus = new ArrayList<>();
        kullandigiCihazs = new ArrayList<>();
    }


    public void getTcKimlik(boolean isCompanion) {
        if (this.getSelected() != null) {

            try {
                String tcKimlikNo = this.getSelected().getEyKisi().getGnlKisi().getTcKimlikNo();
                if (StringUtil.isBlank(tcKimlikNo)) {
                    return;
                }
                if (isCompanion) {
                    tcKimlikNo = this.getSelected().getRefakatciTcKimlikNo();
                }

                GnlKisi gnlKisi = kisiService.findByTckimlikNo(tcKimlikNo);
                if (gnlKisi == null) {
                    if (isCompanion) {
                        gnlKisi = new GnlKisi();
                    } else {
                        gnlKisi = this.getSelected().getEyKisi().getGnlKisi();
                    }
                }

                if (isCompanion) {
                    gnlKisi = kpsController.findByTcKimlikNo(gnlKisi, EnumModul.MORAL_EVI);
                    this.getSelected().setRefakatciAd(gnlKisi.getAd());
                    this.getSelected().setRefakatciAdres(gnlKisi.getAdres());
                    this.getSelected().setRefakatciTelefon(gnlKisi.getTelefon());
                    this.getSelected().setRefakatciSoyad(gnlKisi.getSoyad());
                } else {
                    EyKisi eyKisi = eyKisiService.findByTcKimlikNo(tcKimlikNo);
                    if (eyKisi == null) eyKisi = new EyKisi();
                    gnlKisi = kpsController.findByTcKimlikNo(gnlKisi, EnumModul.MORAL_EVI);
                    eyKisi.setGnlKisi(gnlKisi);
                    this.getSelected().setEyKisi(eyKisi);
                    engelGrubus = Function.filterAndCollect(eyKisi.getEyKisiEngelGrubuList(), EyKisiEngelGrubu::isSecili,
                            EyKisiEngelGrubu::getEyEngelGrubu);
                    faydalandigiHaklars = Function.filterAndCollect(gnlKisi.getGnlKisiFaydalandigiHaklarList(), GnlKisiFaydalandigiHaklar::isSecili,
                            GnlKisiFaydalandigiHaklar::getTanim);
                    maddeKullanimis = Function.filterAndCollect(eyKisi.getEyKisiMaddeKullanimiList(), EyKisiMaddeKullanimi::isSecili,
                            EyKisiMaddeKullanimi::getTanim);
                    aileninGelirKaynagis = Function.filterAndCollect(gnlKisi.getGnlKisiGelirKaynagiList(), GnlKisiGelirKaynagi::isSecili,
                            GnlKisiGelirKaynagi::getTanim);
                    yardimAlinanYerlers = Function.filterAndCollect(gnlKisi.getGnlKisiYardimAlinanYerlerList(), GnlKisiYardimAlinanYerler::isSecili,
                            GnlKisiYardimAlinanYerler::getTanim);
                    yardimTurus = Function.filterAndCollect(gnlKisi.getGnlKisiAldigiYardimlarList(), GnlKisiAldigiYardimlar::isSecili,
                            GnlKisiAldigiYardimlar::getTanim);
                    kullandigiCihazs = Function.filterAndCollect(eyKisi.getEyKisiKullandigiCihazList(), EyKisiKullandigiCihaz::isSecili,
                            EyKisiKullandigiCihaz::getTanim);

                }
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void update(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                service.update(this.getSelected(), engelGrubus, faydalandigiHaklars, maddeKullanimis, aileninGelirKaynagis, yardimAlinanYerlers, yardimTurus, kullandigiCihazs);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                clearList();
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void nviUpdate(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                if (this.getSelected().getEyKisi().getGnlKisi().getDogumTarihi() == null) {
                    FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected().getEyKisi().getGnlKisi(), EnumModul.MORAL_EVI);
                kisiService.edit(kisi);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
                log.error(null);
            }
        }
    }

    public void readInfo() {

    }

    public void meKisiSecKapat(MeKisi meKisi) {
        PrimeFaces.current().dialog().closeDynamic(meKisi);
    }

    public void onRowDblSelect(SelectEvent<MeKisi> event) {
        MeKisi meKisi = event.getObject();
        meKisiSecKapat(meKisi);
    }
}
