package tr.bel.gaziantep.bysweb.moduls.ileriyas.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.VefatEdenKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.ileriyas.entity.IyKisi;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KpsService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.controller.MezarlikSorgulaService;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEden;
import tr.bel.gaziantep.bysweb.webservice.mezarlik.model.VefatEdenRoot;

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
 * @version 1.10.0
 * @since 11.06.2026 08:42
 */
@Named
@ViewScoped
@Slf4j
public class IyKisiController extends AbstractController<IyKisi> {

    @Serial
    private static final long serialVersionUID = -7391188171619610742L;

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
    @Push(channel = "iyKisiChannel")
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

    public IyKisiController() {
        super(IyKisi.class);
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

    @Override
    public IyKisi prepareCreate(ActionEvent event) {
        IyKisi newItem;
        try {
            newItem = IyKisi.class.getDeclaredConstructor().newInstance();
            newItem.setGnlKisi(GnlKisi.builder().build());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void getTcKimlik() {
        try {
            if (this.getSelected() != null) {
                int age = DateUtil.calculateAge(this.getSelected().getGnlKisi().getDogumTarihi());
                if (age < 60) {
                    FacesUtil.addExclamationMessage("60 yaşından küçük olduğundan dolayı kayıt eklenemez...");
                    this.getSelected().setGnlKisi(GnlKisi.builder().build());
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected().getGnlKisi(), EnumModul.ILERI_YAS);
                if (kisi != null) {
                    this.getSelected().setGnlKisi(kisi);
                }

            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void create(ActionEvent event) {
        if (this.getSelected() != null) {
            boolean exist = service.existByTcKimlikNo(this.getSelected().getGnlKisi().getTcKimlikNo());
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
                if (this.getSelected().getGnlKisi().getDogumTarihi() == null) {
                    FacesUtil.errorMessage(Constants.HATA_OLUSTU);
                    return;
                }
                GnlKisi kisi = kpsController.findByTcKimlikNo(this.getSelected().getGnlKisi(), EnumModul.ILERI_YAS);
                this.getSelected().setGnlKisi(kisi);
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
                    if (StringUtils.isNotBlank(coordinate) && !coordinate.equals("null,null")) {
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
}
