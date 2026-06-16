package tr.bel.gaziantep.bysweb.moduls.genel.controller;

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
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.*;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.VefatEdenKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.service.IyKisiService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.controller.GaziKartService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisModel;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KpsService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.controller.MezarlikSorgulaService;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEden;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEdenRoot;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 14:23
 */
@Named
@ViewScoped
@Slf4j
public class GnlKisiController extends AbstractController<GnlKisi> {

    @Serial
    private static final long serialVersionUID = -3129600631919874698L;

    @Inject
    private GnlKisiService service;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private KpsController kpsController;
    @Inject
    private ModelConverter converter;
    @Inject
    private InitApp initApp;
    @Inject
    private IyKisiService iyKisiService;
    @Inject
    @Push(channel = "gnlKisiChannel")
    private PushContext pushContext;

    @Getter
    @Setter
    private int recordCount;
    @Getter
    @Setter
    private int count;
    @Getter
    @Setter
    private LocalDate startDate = LocalDate.now().minusDays(7);
    @Getter
    @Setter
    private LocalDate endDate = LocalDate.now();
    @Getter
    @Setter
    private List<LocalDate> range;
    @Getter
    @Setter
    private LocalDate maxDate;
    private String post;
    @Getter
    private volatile boolean cancelled;

    public GnlKisiController() {
        super(GnlKisi.class);
        maxDate = LocalDate.now();
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case UYRUK -> {
                return filterOptionService.getGnlUyruks();
            }
            case IL -> {
                return filterOptionService.getGnlIls();
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
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    public void getTcKimlik() {
        try {
            if (this.getSelected() != null) {
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected(), EnumModul.GENEL);
                this.setSelected(kisi);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void create(ActionEvent event) {
        if (this.getSelected() != null) {
            boolean exist = service.existByTcKimlikNo(this.getSelected().getTcKimlikNo());
            if (exist) {
                FacesUtil.errorMessage("mukerrerKisiKayit");
                return;
            }
            saveNew(event);
        }
    }

    public void nviUpdate(ActionEvent event) {
        try {
            if (this.getSelected() != null) {
                if (this.getSelected().getDogumTarihi() == null) {
                    FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected(), EnumModul.GENEL);
                this.setSelected(kisi);
                this.save(event);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void updateAdress() {
        if (recordCount > 0) {
            count = 0;
            String post = "";
            try {
                List<GnlKisi> kisiList = service.findByLatLngIsNull(recordCount);

                for (GnlKisi gnlKisi : kisiList) {
                    String coordinate = converter.addLatLng(gnlKisi.getBinaNo());
                    if (StringUtil.isNotBlank(coordinate) && !coordinate.equals("null,null")) {
                        gnlKisi.setLatLng(coordinate);
                        service.updateLatLng(gnlKisi);
                        count++;
                        post = gnlKisi.getAdSoyad() + "," + gnlKisi.getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        pushContext.send(post);
                    } else {
                        count++;
                        post = "Uyarı :Adres Bulunamadı" + "," + gnlKisi.getAdSoyad() + "-" + gnlKisi.getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        pushContext.send(post);
                    }
                }
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
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
        KisiParameters parameters = gnlKisiListToParameters(gnlKisis);

        List<KpsModel> kpsModels = kpsService.getKpsFull(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameters);
        for (KpsModel kpsModel : kpsModels) {
            if (kpsModel.getKutukModel().getHataBilgisi() != null) {
                continue;
            }
            GnlKisi kisi = gnlKisis.stream()
                    .filter(x -> x.getTcKimlikNo().equals(kpsModel.getKutukModel().getTcKimlikNo().toString()))
                    .findFirst()
                    .orElse(null);
            if (kisi != null) {
                kisi = converter.convertKpsModelToGnlKisi(kisi, kpsModel, EnumModul.GENEL);
                kisi.setLatLng(converter.addLatLng(kisi.getBinaNo()));
                kisi.setMernisGuncellemeTarihi(LocalDateTime.now());
                service.edit(kisi);
                count++;
                String post = kisi.getAdSoyad() + "," + kisi.getTcKimlikNo() + "," + count + "," + recordCount;
                pushContext.send(post);
            }
        }
    }

    public KisiParameters gnlKisiListToParameters(List<GnlKisi> kisilers) {
        KisiParameters kisiParameters = new KisiParameters();
        kisiParameters.setKisiler(new ArrayList<>());
        for (GnlKisi kisi : kisilers) {
            KisiParameter parameter = new KisiParameter();
            parameter.setTcKimlikNo(Long.parseLong(kisi.getTcKimlikNo()));
            parameter.setDogumYil(kisi.getDogumTarihi().getYear());
            parameter.setDogumAy(kisi.getDogumTarihi().getMonthValue());
            parameter.setDogumGun(kisi.getDogumTarihi().getDayOfMonth());
            kisiParameters.getKisiler().add(parameter);
        }
        return kisiParameters;
    }

    public void updateDeads() {
        try {
            if (startDate != null && endDate != null) {
                MezarlikSorgulaService mezarlikSorgulaService = new MezarlikSorgulaService();
                VefatEdenRoot vefatEdenRoot = mezarlikSorgulaService.vefatEdenSorgula(initApp.getProperty("webServisLink"),
                        initApp.getProperty("webServisToken"),
                        DateUtil.localdateToString(startDate, "dd.MM.yyyy"),
                        DateUtil.localdateToString(endDate, "dd.MM.yyyy")
                );

                List<VefatEdenKisi> vefatEdenKisiList = new ArrayList<>();
                if (vefatEdenRoot.getHata() == null) {
                    for (VefatEden vefatEden : vefatEdenRoot.getListe()) {
                        LocalDate tarih = vefatEden.getOlumTarihi() == null ? vefatEden.getDefinTarihi().toLocalDate() : vefatEden.getOlumTarihi().toLocalDate();
                        vefatEdenKisiList.add(new VefatEdenKisi(vefatEden.getTcKimlikNo(), tarih));
                    }
                }

                if (vefatEdenKisiList.size() > 2100) {
                    throw new Exception("Liste Boyutu büyük tarih aralığını değiştirerek yeniden deneyiniz");
                }

                if (!vefatEdenKisiList.isEmpty()) {
                    List<String> tcList = vefatEdenKisiList.stream().map(VefatEdenKisi::getTcKimlikNo).collect(Collectors.toList());
                    List<GnlKisi> kisilers = service.findByTcKimlikNoListToList(tcList);
                    int guncellenenKayitSayisi = 0;
                    String post;
                    for (GnlKisi kisi : kisilers) {

                        VefatEdenKisi vefatEdenKisi =
                                vefatEdenKisiList.stream().filter(x -> x.getTcKimlikNo().equals(kisi.getTcKimlikNo())).findFirst().orElse(null);
                        if (vefatEdenKisi == null) continue;
                        kisi.setOlumTarihi(vefatEdenKisi.getOlumTarihi());
                        kisi.setDurum(EnumGnlDurum.OLU);
                        service.edit(kisi);
                        guncellenenKayitSayisi++;
                        post = kisi.getTcKimlikNo() + "," + kisi.getAdSoyad() + "," + guncellenenKayitSayisi + "," + kisilers.size();
                        pushContext.send(post);
                    }

                    FacesUtil.successMessage("islemTamamlandi");
                } else {
                    FacesUtil.warningMessage("kayitBulunamadigindanGuncellemeYapilmadi");
                }

                PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
            }
        } catch (Exception ex) {
            log.error(null);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void gnlKisiSecKapat(GnlKisi gnlKisi) {
        PrimeFaces.current().dialog().closeDynamic(gnlKisi);
    }

    public void onRowDblSelect(SelectEvent<GnlKisi> event) {
        GnlKisi gnlKisi = event.getObject();
        gnlKisiSecKapat(gnlKisi);
    }

//    public void updateFromGaziKart() {
//        try {
//            if (range.size() != 2) {
//                return;
//            }
//            post = ",,,,Servisten bilgiler Okunuyor";
//            pushContext.send(post);
//            GaziKartService gaziKartService = new GaziKartService();
//            String format = "yyyyMMdd";
//            ServisSonucu sonuc = gaziKartService.getList(initApp.getProperty("gazikart.webServisLink"), DateUtil.localdateToString(range.get(0),
//                    format), DateUtil.localdateToString(range.get(1), format));
//            if (sonuc != null && sonuc.getData() != null) {
//                post = ",,,,Servisten bilgiler okundu";
//                pushContext.send(post);
//                count = 0;
//                addTcKimlik(sonuc.getData(), EnumModul.GAZIKART);
//                PrimeFaces.current().executeScript("PF('TarihSecDialog').hide()");
//            }
//            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
//        } catch (Exception ex) {
//            log.error(null, ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//        } finally {
//            PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
//        }
//    }
//
//    private void addTcKimlik(List<ServisModel> servisModels, EnumModul department) throws Exception {
//        post = ",,,,Mernis için parametreler oluşturuluyor";
//        pushContext.send(post);
//        List<KisiParameter> kisiParameterList = converter.servisModelToKisiParametersYasli(servisModels, department);
//        if (kisiParameterList.isEmpty()) {
//            return;
//        }
//        List<List<KisiParameter>> hasNotTcKimlikSplitList = ListUtil.partition(kisiParameterList, 100);
//        List<String> hasNotTcKimlikList = new ArrayList<>();
//
//        for (List<KisiParameter> kisiParameters : hasNotTcKimlikSplitList) {
//            List<String> tcNewList = kisiParameters.stream().map(parameter -> parameter.getTcKimlikNo() + "").toList();
//            hasNotTcKimlikList.addAll(service.findByTcKimlikNoList(tcNewList));
//        }
//
//        for (String str : hasNotTcKimlikList) {
//            kisiParameterList.removeIf(x -> x.getTcKimlikNo() == Long.parseLong(str));
//        }
//
//        List<List<KisiParameter>> splitList = ListUtil.partition(kisiParameterList, 70);
//        recordCount = kisiParameterList.size();
//        post = ",,,,Mernis için parametreler oluşturuldu";
//        pushContext.send(post);
//        KpsService kpsService = new KpsService();
//        post = ",,,,Mernis'ten sorgulama yapmak için hazırlık yapılıyor";
//        pushContext.send(post);
//        for (List<KisiParameter> kisiParameters : splitList) {
//            KisiParameters parameters = new KisiParameters();
//            parameters.setKisiler(kisiParameters);
//            List<KpsModel> kpsModels = kpsService.getKpsFull(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameters);
//            addPerson(servisModels, kpsModels, department);
//
//        }
//    }
//
//    private void addPerson(List<ServisModel> servisModels, List<KpsModel> kpsModels, EnumModul modul) throws Exception {
//
//        for (KpsModel kpsModel : kpsModels) {
//            GnlKisi gnlKisi = service.findByTckimlikNo(kpsModel.getKutukModel().getTcKimlikNo().toString());
//            if (gnlKisi == null) {
//                gnlKisi = new GnlKisi();
//            }
//            gnlKisi = converter.convertKpsModelToGnlKisi(gnlKisi, kpsModel, modul);
//            ServisModel servisModel = isExistServisModel(gnlKisi.getTcKimlikNo(), servisModels);
//
//            if (servisModel != null) {
//                String phoneNumber = Function.phoneValidate(servisModel.getTelMobile());
//                gnlKisi.setTelefon(phoneNumber);
//
//                IyKisi iyKisi = iyKisiService.findByTcKimlikNo(gnlKisi.getTcKimlikNo());
//                if (iyKisi == null) {
//                    iyKisi = IyKisi.builder().gnlKisi(gnlKisi).build();
//                    iyKisiService.create(iyKisi);
//                    gnlKisi.setYasli(true);
//                }
//                service.edit(gnlKisi);
//                count++;
//                post = gnlKisi.getAdSoyad() + "," + gnlKisi.getTcKimlikNo() + "," + count + "," + recordCount + ", ";
//                pushContext.send(post);
//            }
//        }
//    }
//
//    private ServisModel isExistServisModel(String tcKimlikNo, List<ServisModel> servisModels) {
//        return servisModels.stream()
//                .filter(servisModel -> StringUtil.isNotBlank(servisModel.getIdentityNo()) && servisModel.getIdentityNo().equals(tcKimlikNo))
//                .findFirst()
//                .orElse(null);
//    }

//    public void updateFromGaziKart() {
//        if (range.size() != 2) return;
//
//        try {
//            count=0;
//            pushContext.send("Servisten bilgiler Okunuyor");
//
//            GaziKartService gaziKartService = new GaziKartService();
//            String format = "yyyyMMdd";
//            String start = DateUtil.localdateToString(range.get(0), format);
//            String end = DateUtil.localdateToString(range.get(1), format);
//            ServisSonucu sonuc = gaziKartService.getList(initApp.getProperty("gazikart.webServisLink"), start, end);
//
//            if (sonuc == null || sonuc.getData() == null) {
//                FacesUtil.warningMessage("Servisten veri alınamadı");
//                return;
//            }
//
//            pushContext.send("Servisten bilgiler okundu");
//            processGaziKartData(sonuc.getData());
//            PrimeFaces.current().executeScript("PF('TarihSecDialog').hide()");
//            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
//        } catch (Exception ex) {
//            log.error(null, ex);
//            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//        } finally {
//            PrimeFaces.current().executeScript("PF('KisiMernisListeGuncelle').hide()");
//        }
//    }

    public void cancelGaziKartUpdate() {
        cancelled = true;
    }

    public void updateFromGaziKart() {
        if (range.size() != 2) return;


        LocalDate start = range.get(0);
        LocalDate end = range.get(1);
        cancelled = false;
        count = 0;

        new Thread(() -> {
            try {
                Integer kullaniciId = this.getSyKullanici().getId();
                pushContext.send("Servisten bilgiler Okunuyor");

                GaziKartService gaziKartService = new GaziKartService();
                String format = "yyyyMMdd";
                ServisSonucu sonuc = gaziKartService.getList(
                        initApp.getProperty("gazikart.webServisLink"),
                        DateUtil.localdateToString(start, format),
                        DateUtil.localdateToString(end, format)
                );

                if (cancelled) {
                    pushContext.send("CANCELLED", kullaniciId);
                    return;
                }

                if (sonuc == null || sonuc.getData() == null) {
                    pushContext.send("Servisten veri alınamadı", kullaniciId);
                    pushContext.send("COMPLETED", kullaniciId);
                    return;
                }

                pushContext.send("Servisten bilgiler okundu", kullaniciId);
                processGaziKartDataBg(sonuc.getData());

                if (!cancelled) {
                    pushContext.send("COMPLETED", kullaniciId);
                }
            } catch (Exception ex) {
                log.error(null, ex);
                pushContext.send("Hata: " + ex.getMessage());
                pushContext.send("COMPLETED");
            }
        }).start();

    }

    private void processGaziKartDataBg(List<ServisModel> servisModels) {
        try {
            pushContext.send("KPS için parametreler oluşturuluyor");

            List<KisiParameter> allParams = converter.servisModelToKisiParametersYasli(servisModels, EnumModul.GAZIKART);
            if (allParams.isEmpty() || cancelled) {
                if (cancelled) pushContext.send("CANCELLED", this.getSyKullanici().getId());
                return;
            }

            Map<String, ServisModel> servisMap = buildServisMap(servisModels);
            List<KisiParameter> newParams = filterExistingTcKimlikNos(allParams);

            if (cancelled) {
                pushContext.send("CANCELLED", this.getSyKullanici().getId());
                return;
            }

            if (newParams.isEmpty()) {
                pushContext.send("Tüm kayıtlar zaten mevcut");
                return;
            }

            pushContext.send("MERNIS sorgulanıyor: " + newParams.size() + " kişi");
            recordCount = newParams.size();
            syncWithMernisBg(newParams, servisMap);
        } catch (Exception ex) {
            log.error(null, ex);
            pushContext.send("Hata: " + ex.getMessage(), this.getSyKullanici().getId());
        }
    }

    private Map<String, ServisModel> buildServisMap(List<ServisModel> servisModels) {
        return servisModels.stream()
                .filter(s -> StringUtil.isNotBlank(s.getIdentityNo()))
                .collect(Collectors.toMap(ServisModel::getIdentityNo, s -> s, (a, b) -> a));
    }

    private List<KisiParameter> filterExistingTcKimlikNos(List<KisiParameter> params) {
        List<String> tcList = params.stream()
                .map(p -> String.valueOf(p.getTcKimlikNo()))
                .toList();

        Set<String> existingTcSet = new HashSet<>(service.findByTcKimlikNoList(tcList));

        return params.stream()
                .filter(p -> !existingTcSet.contains(String.valueOf(p.getTcKimlikNo())))
                .toList();
    }

    private void syncWithMernisBg(List<KisiParameter> params, Map<String, ServisModel> servisMap) {
        try {
            KpsService kpsService = new KpsService();
            String wsLink = initApp.getProperty("webServisLink");
            String wsToken = initApp.getProperty("webServisToken");

            List<List<KisiParameter>> batches = ListUtil.partition(params, 70);

            for (List<KisiParameter> batch : batches) {
                if (cancelled) {
                    pushContext.send("CANCELLED", this.getSyKullanici().getId());
                    return;
                }

                KisiParameters parameters = new KisiParameters();
                parameters.setKisiler(batch);

                List<KpsModel> kpsModels = kpsService.getKpsByKutukByAdres(wsLink, wsToken, parameters);
                savePersonsBg(kpsModels, servisMap);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            pushContext.send("Hata: " + ex.getMessage(), this.getSyKullanici().getId());
        }
    }

    private void savePersonsBg(List<KpsModel> kpsModels, Map<String, ServisModel> servisMap) throws Exception {
        for (KpsModel kpsModel : kpsModels) {
            if (cancelled) {
                pushContext.send("CANCELLED", this.getSyKullanici());
                return;
            }
            if(kpsModel.getKutukModel().getTcKimlikNo()==null){
                count++;
                continue;
            }
            String tcKimlikNo = kpsModel.getKutukModel().getTcKimlikNo().toString();

            ServisModel servisModel = servisMap.get(tcKimlikNo);
            if (servisModel == null) {
                count++;
                continue;
            }

            GnlKisi gnlKisi = converter.convertKpsModelToGnlKisi(new GnlKisi(), kpsModel, EnumModul.GAZIKART);

            String phone = Function.phoneValidate(servisModel.getTelMobile());
            gnlKisi.setTelefon(phone);

            IyKisi iyKisi = iyKisiService.findByTcKimlikNo(tcKimlikNo);
            if (iyKisi == null) {
                iyKisi = IyKisi.builder().gnlKisi(gnlKisi).build();
                iyKisiService.create(iyKisi);
                gnlKisi.setYasli(true);
            }
            service.edit(gnlKisi);

            count++;
            pushContext.send(gnlKisi.getAdSoyad() + "," + tcKimlikNo + "," + count + "," + recordCount + ",");
        }
    }


}
