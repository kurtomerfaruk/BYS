package tr.bel.gaziantep.bysweb.moduls.engelsizler.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.converter.ModelConverter;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyAnketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyKullandigiCihaz;
import tr.bel.gaziantep.bysweb.core.enums.engelsizler.EnumEyMaddeKullanimi;
import tr.bel.gaziantep.bysweb.core.enums.genel.*;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyEngelGrubuService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyTalepService;
import tr.bel.gaziantep.bysweb.moduls.genel.controller.GnlKisiController;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.*;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.controller.GaziKartService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisModel;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KpsService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 23.06.2025 09:17
 */
@Named
@ViewScoped
@Slf4j
public class EyKisiController extends AbstractController<EyKisi> {

    @Serial
    private static final long serialVersionUID = 6858724710217059235L;

    @Inject
    private EyKisiService service;
    @Inject
    private EyEngelGrubuService engelGrubuService;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private EyTalepService eyTalepService;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private KpsController kpsController;
    @Inject
    private InitApp initApp;
    @Inject
    private ModelConverter converter;
    @Inject
    private GnlKisiController gnlKisiController;
    @Inject
    @Push(channel = "eyKisiChannel")
    private PushContext pushContext;

//    private SyKullanici syKullanici;
//    @Getter
//    @Setter
//    private EyKisiRapor eyKisiRapor;
    @Getter
    @Setter
    private LocalDate maxDate;
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
    private String qrText;
    @Getter
    @Setter
    private String qrLabel;
    private String post;
    @Getter
    @Setter
    private LocalDate baslangicTarihi;
    @Getter
    @Setter
    private LocalDate bitisTarihi;
    private int count;
    @Getter
    @Setter
    private int recordCount;
    @Getter
    @Setter
    private EyTalep eyTalep;

//    private List<EyKisi> selecteds ;
//
    public EyKisiController() {
        super(EyKisi.class);
    }
//
//    public List<EyKisi> getSelecteds() {
//        return selecteds;
//    }
//
//    public void setSelecteds(List<EyKisi> selecteds) {
//        this.selecteds = selecteds;
//    }

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
            case EKLEME_YERI -> {
                return filterOptionService.getEklemeYeris();
            }
            case ENGELSIZ_ANKET_DURUMU -> {
                return filterOptionService.getEyAnketDurumus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @PostConstruct
    @Override
    public void init() {
//        syKullanici = Util.getSyKullanici();
//        this.setTableName("EyKisi");
//        readColumns(syKullanici);
        super.init();
        maxDate = LocalDate.now();
    }

    @Override
    public EyKisi prepareCreate(ActionEvent event) {
        EyKisi newItem;
        try {
            newItem = EyKisi.class.getDeclaredConstructor().newInstance();
            newItem.setAnketDurumu(EnumEyAnketDurumu.ANKET_YAPILMADI);
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .sosyalYardimAliyorMu(false)
                    .eklemeYeri(EnumModul.ENGELSIZLER)
                    .gnlIl(new GnlIl())
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .build();
            newItem.setGnlKisi(kisi);
            newItem.setIrtibatKuranGnlPersonel(this.getSyKullanici().getGnlPersonel());
//            eyKisiRapor = new EyKisiRapor();
//            eyKisiRapor.setEyKisi(newItem);
            clearList();
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void update(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                EyKisi existing = service.findByKisi(this.getSelected().getGnlKisi());

                if(existing != null && !existing.getId().equals(this.getSelected().getId())) {
                    FacesUtil.addErrorMessage("Bu kişiye ait zaten Engelli kaydı var.");
                    return;
                }

                service.update(this.getSelected(), engelGrubus, faydalandigiHaklars, maddeKullanimis, aileninGelirKaynagis, yardimAlinanYerlers, yardimTurus, kullandigiCihazs);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void getTcKimlik() {
        try {
            if (this.getSelected() != null) {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.ENGELSIZLER);
                if (kisi != null) {
                    this.getSelected().setGnlKisi(kisi);
                }

            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }

    public void clearList() {
        engelGrubus = new ArrayList<>();
        maddeKullanimis = new ArrayList<>();
        aileninGelirKaynagis = new ArrayList<>();
        yardimAlinanYerlers = new ArrayList<>();
        yardimTurus = new ArrayList<>();
        kullandigiCihazs = new ArrayList<>();
    }

    public void readInfo() {
//        if (this.getSelected() != null) {
//            List<EyKisiEngelGrubu> eyKisiEngelgrubuList = this.getSelected().getEyKisiEngelGrubuList();
//            engelGrubus = new ArrayList<>();
//            eyKisiEngelgrubuList.stream().filter(EyKisiEngelGrubu::isSecili).forEachOrdered(kisiEngelgrubu -> engelGrubus.add(kisiEngelgrubu.getEyEngelGrubu()));
//
//            List<EyKisiMaddeKullanimi> eyKisiMaddeKullanimiList = this.getSelected().getEyKisiMaddeKullanimiList();
//            maddeKullanimis = new ArrayList<>();
//            eyKisiMaddeKullanimiList.stream().filter(EyKisiMaddeKullanimi::isSecili).forEachOrdered(x -> maddeKullanimis.add(x.getTanim()));
//
//            List<GnlKisiGelirKaynagi> gnlKisiGelirKaynagiList = this.getSelected().getGnlKisi().getGnlKisiGelirKaynagiList();
//            aileninGelirKaynagis = new ArrayList<>();
//            gnlKisiGelirKaynagiList.stream().filter(GnlKisiGelirKaynagi::isSecili).forEachOrdered(x -> aileninGelirKaynagis.add(x.getTanim()));
//
//            List<GnlKisiYardimAlinanYerler> gnlKisiYardimAlinanYerlerList = this.getSelected().getGnlKisi().getGnlKisiYardimAlinanYerlerList();
//            yardimAlinanYerlers = new ArrayList<>();
//            gnlKisiYardimAlinanYerlerList.stream().filter(GnlKisiYardimAlinanYerler::isSecili).forEachOrdered(x -> yardimAlinanYerlers.add(x.getTanim()));
//
//            List<GnlKisiAldigiYardimlar> gnlKisiAldigiYardimlarList = this.getSelected().getGnlKisi().getGnlKisiAldigiYardimlarList();
//            yardimTurus = new ArrayList<>();
//            gnlKisiAldigiYardimlarList.stream().filter(GnlKisiAldigiYardimlar::isSecili).forEachOrdered(x -> yardimTurus.add(x.getTanim()));
//
//            List<EyKisiKullandigiCihaz> eyKisiKullandigiCihazList = this.getSelected().getEyKisiKullandigiCihazList();
//            kullandigiCihazs = new ArrayList<>();
//            eyKisiKullandigiCihazList.stream().filter(EyKisiKullandigiCihaz::isSecili).forEachOrdered(x -> kullandigiCihazs.add(x.getTanim()));
//
//            if (this.getSelected().getIrtibatKuranGnlPersonel() == null) {
//                this.getSelected().setIrtibatKuranGnlPersonel(syKullanici.getGnlPersonel());
//            }
//        }
        if (this.getSelected() != null) {
            // Ortak filtreleme işlemini metodlaştırıyoruz.
            engelGrubus = Function.filterAndCollect(this.getSelected().getEyKisiEngelGrubuList(), EyKisiEngelGrubu::isSecili, EyKisiEngelGrubu::getEyEngelGrubu);
            faydalandigiHaklars = Function.filterAndCollect(this.getSelected().getGnlKisi().getGnlKisiFaydalandigiHaklarList(), GnlKisiFaydalandigiHaklar::isSecili,
                    GnlKisiFaydalandigiHaklar::getTanim);
            maddeKullanimis = Function.filterAndCollect(this.getSelected().getEyKisiMaddeKullanimiList(), EyKisiMaddeKullanimi::isSecili, EyKisiMaddeKullanimi::getTanim);
            aileninGelirKaynagis = Function.filterAndCollect(this.getSelected().getGnlKisi().getGnlKisiGelirKaynagiList(), GnlKisiGelirKaynagi::isSecili, GnlKisiGelirKaynagi::getTanim);
            yardimAlinanYerlers = Function.filterAndCollect(this.getSelected().getGnlKisi().getGnlKisiYardimAlinanYerlerList(), GnlKisiYardimAlinanYerler::isSecili, GnlKisiYardimAlinanYerler::getTanim);
            yardimTurus = Function.filterAndCollect(this.getSelected().getGnlKisi().getGnlKisiAldigiYardimlarList(), GnlKisiAldigiYardimlar::isSecili, GnlKisiAldigiYardimlar::getTanim);
            kullandigiCihazs = Function.filterAndCollect(this.getSelected().getEyKisiKullandigiCihazList(), EyKisiKullandigiCihaz::isSecili, EyKisiKullandigiCihaz::getTanim);

            if (this.getSelected().getIrtibatKuranGnlPersonel() == null) {
                this.getSelected().setIrtibatKuranGnlPersonel(this.getSyKullanici().getGnlPersonel());
            }
        }
    }

    public void nviUpdate(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                if (this.getSelected().getGnlKisi().getDogumTarihi() == null) {
                    FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected().getGnlKisi(), EnumModul.ENGELSIZLER);
                gnlKisiService.edit(kisi);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }

    public void createQRCode() {
        if (this.getSelected() != null) {
            String latLng = this.getSelected().getGnlKisi().getLatLng();
            if (!StringUtil.isBlank(latLng)) {
                qrText = "https://www.google.com/maps/search/?api=1&query=" + latLng;
                qrLabel = this.getSelected().getGnlKisi().getAdSoyad();
                PrimeFaces.current().executeScript("PF('QrCodeDialog').show()");
            } else {
                FacesUtil.addExclamationMessage("Kişinin adres bilgisi olmadığından QRCode oluşturulamadı!!!");
            }
        }
    }

    public void updateFromGaziKart() {
        try {
            post = ",,,,Servisten bilgiler Okunuyor";
            pushContext.send(post);
            GaziKartService gaziKartService = new GaziKartService();
            String format = "yyyyMMdd";
            ServisSonucu sonuc = gaziKartService.getList(initApp.getProperty("gazikart.webServisLink"), DateUtil.localdateToString(baslangicTarihi,
                    format), DateUtil.localdateToString(bitisTarihi, format));
            if (sonuc != null && sonuc.getData() != null) {
                post = ",,,,Servisten bilgiler okundu";
                pushContext.send(post);
                count = 0;
                addTcKimlik(sonuc.getData(), EnumModul.GAZIKART);
                PrimeFaces.current().executeScript("PF('TarihSecDialog').hide()");
            }
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        } finally {
            PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
        }
    }

    private void addTcKimlik(List<ServisModel> servisModels, EnumModul department) throws Exception {
        post = ",,,,Mernis için parametreler oluşturuluyor";
        pushContext.send(post);
        List<KisiParameter> kisiParameterList = converter.servisModelToKisiParameters(servisModels, department);
        if (kisiParameterList.isEmpty()) {
            return;
        }
        List<List<KisiParameter>> hasNotTcKimlikSplitList = ListUtil.partition(kisiParameterList, 100);
        List<String> hasNotTcKimlikList = new ArrayList<>();

        for (List<KisiParameter> kisiParameters : hasNotTcKimlikSplitList) {
            List<String> tcNewList = kisiParameters.stream().map(parameter -> parameter.getTcKimlikNo() + "").collect(Collectors.toList());
            hasNotTcKimlikList.addAll(service.findByTcKimlikNoList(tcNewList));
        }

        for (String str : hasNotTcKimlikList) {
            kisiParameterList.removeIf(x -> x.getTcKimlikNo() == Long.parseLong(str));
        }

        List<List<KisiParameter>> splitList = ListUtil.partition(kisiParameterList, 70);
        recordCount = kisiParameterList.size();
        post = ",,,,Mernis için parametreler oluşturuldu";
        pushContext.send(post);
        KpsService kpsService = new KpsService();
        post = ",,,,Mernis'ten sorgulama yapmak için hazırlık yapılıyor";
        pushContext.send(post);
        for (List<KisiParameter> kisiParameters : splitList) {

            List<String> tcListSplit = kisiParameters.stream().map(x -> x.getTcKimlikNo() + "").toList();
            KisiParameters parameters = new KisiParameters();
            parameters.setKisiler(kisiParameters);
            List<KpsModel> kpsModels = kpsService.getKpsFull(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameters);
            addPerson(servisModels, kpsModels, EnumModul.ENGELSIZLER);

        }
    }

    private void addPerson(List<ServisModel> servisModels, List<KpsModel> kpsModels, EnumModul modul) throws Exception {

        for (KpsModel kpsModel : kpsModels) {

            List<EyKisiEngelGrubu> eyKisiEngelgrubus = new ArrayList<>();
            EyKisi eyKisi = converter.convertKpsModelToEyKisi(kpsModel, modul);
            ServisModel servisModel = isExistServisModel(eyKisi.getGnlKisi().getTcKimlikNo(), servisModels);

            if (servisModel != null) {
                String phoneNumber = Function.phoneValidate(servisModel.getTelMobile());
                eyKisi.getGnlKisi().setTelefon(phoneNumber);
                String[] disableGroups = servisModel.getDisabledDegree().split("-");
                String disableGroupStr = "";
                if (disableGroups.length == 2) {
                    Integer engelOrani = Integer.parseInt(disableGroups[1].replace("+", ""));
                    eyKisi.setToplamVucutKayipOrani(engelOrani);
                }
                if (disableGroups.length > 0) {
                    disableGroupStr = disableGroups[0].split(" ")[0];
                    if (disableGroupStr.equals("Süregen")) {
                        disableGroupStr = disableGroupStr.replace("g", "ğ");
                    }
                }

                EyEngelGrubu disableGroup = engelGrubuService.findByName(disableGroupStr);
                if (disableGroup != null) {
                    EyKisiEngelGrubu eyKisiEngelgrubu = new EyKisiEngelGrubu();
                    eyKisiEngelgrubu.setEyEngelGrubu(disableGroup);
                    eyKisiEngelgrubu.setEyKisi(eyKisi);
                    eyKisiEngelgrubu.setSecili(Boolean.TRUE);
                    eyKisiEngelgrubus.add(eyKisiEngelgrubu);
                }
                eyKisi.setEyKisiEngelGrubuList(eyKisiEngelgrubus);
                service.edit(eyKisi);
                count++;
                post = eyKisi.getGnlKisi().getAdSoyad() + "," + eyKisi.getGnlKisi().getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                pushContext.send(post);
            }
        }
    }

    private ServisModel isExistServisModel(String tcKimlikNo, List<ServisModel> servisModels) {
        return servisModels.stream()
                .filter(servisModel -> StringUtil.isNotBlank(servisModel.getIdentityNo()) && servisModel.getIdentityNo().equals(tcKimlikNo))
                .findFirst()
                .orElse(null);
    }

    public void updateAdress() {
        if (recordCount > 0) {
            count = 0;
            try {
                List<EyKisi> kisiList = service.findByAddress(recordCount);

                for (EyKisi eyKisi : kisiList) {
                    String coordinate = converter.addLatLng(eyKisi.getGnlKisi().getBinaNo());
                    if (!StringUtil.isBlank(coordinate) && !coordinate.equals("null,null")) {
                        eyKisi.getGnlKisi().setLatLng(coordinate);
                        gnlKisiService.updateLatLng(eyKisi.getGnlKisi());
                        count++;
                        post = eyKisi.getGnlKisi().getAdSoyad() + "," + eyKisi.getGnlKisi().getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        pushContext.send(post);
                    } else {
                        count++;
                        post =
                                "Uyarı :Adres Bulunamadı" + "," + eyKisi.getGnlKisi().getAdSoyad() + "-" + eyKisi.getGnlKisi().getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        pushContext.send(post);
                    }
                }
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
                PrimeFaces.current().executeScript("PF('AdresListeGuncelleDialog').hide()");
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            } finally {
                PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
            }
        }
    }

    public void updateList() {
        try {
            count = 0;
            int limit = recordCount / 70;
            KpsService kpsService = new KpsService();
            if (limit > 0) {
                for (int i = 0; i < limit; i++) {
                    List<GnlKisi> tcList = service.getTcList(70);
                    if (!tcList.isEmpty()) {
                        updateTc(tcList, kpsService);
                    }

                }
            } else {
                List<GnlKisi> tcList = service.getTcList(recordCount);
                if (!tcList.isEmpty()) {
                    updateTc(tcList, kpsService);
                }
            }
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            PrimeFaces.current().executeScript("PF('ListeGuncelleDialog').hide()");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        } finally {
            PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
        }
    }

    private void updateTc(List<GnlKisi> gnlKisis, KpsService kpsService) throws Exception {
        KisiParameters parameters = gnlKisiController.gnlKisiListToParameters(gnlKisis);

        List<KpsModel> kpsModels = kpsService.getKpsFull(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameters);
        for (KpsModel kpsModel : kpsModels) {
            if (kpsModel.getKutukModel().getHataBilgisi() != null) {
                continue;
            }
            GnlKisi kisi = gnlKisis.stream()
                    .filter(x -> !StringUtil.isBlank(x.getTcKimlikNo()) && x.getTcKimlikNo().equals(kpsModel.getKutukModel().getTcKimlikNo().toString()))
                    .findFirst()
                    .orElse(null);
            if (kisi != null) {
                kisi = converter.convertKpsModelToGnlKisi(kisi, kpsModel, EnumModul.ENGELSIZLER);
                kisi.setLatLng(converter.addLatLng(kisi.getBinaNo()));
                kisi.setMernisGuncellemeTarihi(LocalDateTime.now());
                gnlKisiService.edit(kisi);
                count++;
                post = kisi.getAdSoyad() + "," + kisi.getTcKimlikNo() + "," + count + "," + recordCount;
                pushContext.send(post);
            }
        }
    }

    public void addTalep(ActionEvent event) {
        if (eyTalep != null) {
            try {
                if (this.getSelected().getEyTalepList() == null) {
                    this.getSelected().setEyTalepList(new ArrayList<>());
                }
                eyTalepService.create(eyTalep);
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                this.getSelected().getEyTalepList().add(eyTalep);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public EyTalep prepareCreateTalep(ActionEvent event) {
        EyTalep newItem;
        try {
            if (this.getSelected().getEyTalepList() == null) {
                this.getSelected().setEyTalepList(new ArrayList<>());
            }
            newItem = EyTalep.class.getDeclaredConstructor().newInstance();
            newItem.setEyKisi(this.getSelected());
            newItem.setTarih(LocalDateTime.now());
            newItem.setDurum(EnumGnlTalepDurumu.BEKLIYOR);
            this.setEyTalep(newItem);
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
        return null;
    }

    public Boolean resimMi(EyKisiDosya personFile) {
        if (personFile != null) {
            return Function.isImage(personFile.getDosyaAdi());
        }

        return false;
    }

    public void eyKisiSecKapat(EyKisi eyKisi) {
        PrimeFaces.current().dialog().closeDynamic(eyKisi);
    }

//    public void selectAndClose() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("selectedList", selecteds);
//        PrimeFaces.current().dialog().closeDynamic(params);
//    }

    public void onRowDblSelect(SelectEvent<EyKisi> event) {
        EyKisi eyKisi = event.getObject();
        eyKisiSecKapat(eyKisi);
    }

}
