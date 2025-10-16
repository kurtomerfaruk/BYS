package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.ErrorType;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtBasvuruHareketDurumu;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtFizikTedaviDurum;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtMalzemeOnayDurumu;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.ImageUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.*;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.*;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private OrtBasvuruService service;
    @Inject
    private OrtPersonelService personelService;
    @Inject
    private OrtBasvuruMalzemeTeslimiService ortBasvuruMalzemeTeslimiService;
    @Inject
    private OrtMalzemeTalepService malzemeTalepService;
    @Inject
    private OrtFizikTedaviService fizikTedaviService;
    @Inject
    private FilterOptionService filterOptionService;
    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kps;

    @Getter
    @Setter
    private OrtPersonel ortPersonel;
    @Getter
    @Setter
    private boolean addAppointment;
    @Getter
    @Setter
    private LocalDateTime appointmentDate;
    @Getter
    @Setter
    private OrtBasvuruMalzemeTeslimi ortBasvuruMalzemeTeslimi;
    @Getter
    @Setter
    private List<OrtFizikTedavi> fizikTedaviList;
    @Getter
    @Setter
    private int rowIndex;
    @Getter
    @Setter
    private LocalDateTime teslimTarihi;
    @Getter
    @Setter
    private GnlKisi teslimAlanKisi;

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

//    @Override
//    @PostConstruct
//    public void init() {
//        super.init();
//        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
//        if (ortPersonel == null) {
//            FacesUtil.addErrorMessage("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
//            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
//        }
//    }

    public void onPreRenderView() {
        ortPersonel = personelService.findByGnlPersonel(this.getSyKullanici().getGnlPersonel());
        if (ortPersonel == null) {
            throw new BysBusinessException("Sistem yöneticiniz ile görüşüp personel tanımı yaptırınız...");
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
            newItem.setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.BEKLEMEDE);
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
    public void saveNew(ActionEvent event) {
        if (this.getSelected().getOrtHasta() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            service.saveNew(this.getSelected());
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected().getOrtHasta() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            service.save(this.getSelected(), addAppointment, appointmentDate);
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
            service.saveDurum(this.getSelected(), EnumOrtBasvuruHareketDurumu.ODEME_ALINDI);
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
                service.saveDurum(this.getSelected(), EnumOrtBasvuruHareketDurumu.SUT_KODU_VERILDI);
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

    public void prepareSiliconDelivery() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        if (this.getSelected().getOrtBasvuruMalzemeTeslimiList().isEmpty()) {
            ortBasvuruMalzemeTeslimi = OrtBasvuruMalzemeTeslimi.builder()
                    .teslimTarihi(LocalDateTime.now())
                    .ortBasvuru(this.getSelected())
                    .teslimEdenOrtPersonel(ortPersonel)
                    .teslimAlanGnlKisi(new GnlKisi())
                    .ortStok(new OrtStok())
                    .miktar(BigDecimal.ONE)
                    .build();
        } else {
            ortBasvuruMalzemeTeslimi = this.getSelected().getOrtBasvuruMalzemeTeslimiList().get(0);
            if(ortBasvuruMalzemeTeslimi.getTeslimAlanGnlKisi()==null){
                ortBasvuruMalzemeTeslimi.setTeslimAlanGnlKisi(new GnlKisi());
            }
        }
    }

    public void saveSiliconDelivery() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            ortBasvuruMalzemeTeslimiService.delivery(ortBasvuruMalzemeTeslimi, this.getSyKullanici());
            FacesUtil.successMessage("silikonTeslimEdildi");
            //prepareSiliconDelivery();
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void secilenOrtStok(SelectEvent<OrtStok> event) {
        OrtStok ortStok = event.getObject();
        this.ortBasvuruMalzemeTeslimi.setOrtStok(ortStok);
    }

    public void preparePhysioTherapy() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            if (this.getSelected().getOrtHasta().getOrtFizikTedaviList() == null) {
                fizikTedaviList = new ArrayList<>();
            } else {
                fizikTedaviList = this.getSelected().getOrtHasta().getOrtFizikTedaviList();
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }

    }

    public void savePhysioTherapy() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }
        try {
            this.getSelected().setBasvuruHareketDurumu(EnumOrtBasvuruHareketDurumu.FIZIK_TEDAVI_PLANI_OLUSTURULDU);
            fizikTedaviService.persist(fizikTedaviList, this.getSelected());
            FacesUtil.successMessage("fizikTedaviEklendi");
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void onAddNew() {
        OrtFizikTedavi fizikTedavi = OrtFizikTedavi.builder()
                .ortHasta(this.getSelected().getOrtHasta())
                .tarih(LocalDateTime.now())
                .durum(EnumOrtFizikTedaviDurum.BEKLIYOR)
                .build();
        fizikTedaviList.add(fizikTedavi);
    }

    public void onCellEdit(CellEditEvent event) {
    }

    public void onCellEditInit(CellEditEvent event) {
        this.setRowIndex(event.getRowIndex());
    }

    public void removeRow(OrtFizikTedavi detay) {
        try {
            fizikTedaviList.remove(detay);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public boolean isImage(String fileName) {
        return ImageUtil.isImage(fileName);
    }

    public boolean isPdf(String fileName) {
        return ImageUtil.isPdf(fileName);
    }

    public void prepareDelivery() {
        OrtBasvuru selected = this.getSelected();
        if (selected == null) {
            throw new BysBusinessException(ErrorType.NESNE_OKUNAMADI);
        }

        try {
            List<OrtMalzemeTalep> talepler = malzemeTalepService.findByOrtBasvuruByOnayDurum(selected, EnumOrtMalzemeOnayDurumu.ONAYLANDI);

            List<OrtBasvuruMalzemeTeslimi> teslimListesi = selected.getOrtBasvuruMalzemeTeslimiList();

            talepler.stream()
                    .filter(t -> t.getOrtMalzemeTalepStokList() != null)
                    .flatMap(t -> t.getOrtMalzemeTalepStokList().stream())
                    .map(talepStok -> OrtBasvuruMalzemeTeslimi.builder()
                            .ortBasvuru(selected)
                            .ortStok(talepStok.getOrtStok())
                            .miktar(talepStok.getMiktar())
                            .build())
                    .forEach(teslimListesi::add);
            teslimTarihi = LocalDateTime.now();
            teslimAlanKisi = new GnlKisi();
            this.getSelected().setOrtBasvuruMalzemeTeslimiList(teslimListesi);
        } catch (Exception ex) {
            log.error("Teslim hazırlığı sırasında hata oluştu", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void saveDelivery() {
        try {
            service.saveDelivery(this.getSelected(),this.getSyKullanici());
            FacesUtil.successMessage("urunlerTeslimEdildi");
        } catch (Exception ex) {
            log.error("Teslim kayıdı sırasında hata oluştu", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void getTcKimlik() {
        try {
            if (teslimAlanKisi != null) {
                String tcKimlikNo = teslimAlanKisi.getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = teslimAlanKisi;
                kisi = kps.findByTcKimlikNo(kisi, EnumModul.ORTEZ_PROTEZ);
                if (kisi != null) {
                    teslimAlanKisi = kisi;
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
    }

    public void getTcKimlikSilicon() {
        try {
            if (ortBasvuruMalzemeTeslimi.getTeslimAlanGnlKisi() != null) {
                String tcKimlikNo = ortBasvuruMalzemeTeslimi.getTeslimAlanGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = ortBasvuruMalzemeTeslimi.getTeslimAlanGnlKisi();
                kisi = kps.findByTcKimlikNo(kisi, EnumModul.ORTEZ_PROTEZ);
                if (kisi != null) {
                    ortBasvuruMalzemeTeslimi.setTeslimAlanGnlKisi(kisi);
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
        }
    }

    public void updateFields() {
        try {
            List<OrtBasvuruMalzemeTeslimi> teslimList = this.getSelected().getOrtBasvuruMalzemeTeslimiList();

            boolean hasTeslimAlan = teslimAlanKisi != null && StringUtil.isNotBlank(teslimAlanKisi.getAd());
            boolean hasTarih = teslimTarihi != null;
            boolean hasPersonel = ortPersonel != null;


            for (OrtBasvuruMalzemeTeslimi teslim : teslimList) {
                if(teslim.getId()!=null) continue;
                if (hasTeslimAlan) {
                    teslim.setTeslimAlanGnlKisi(teslimAlanKisi);
                }
                if (hasTarih) {
                    teslim.setTeslimTarihi(teslimTarihi);
                }
                if (hasPersonel) {
                    teslim.setTeslimEdenOrtPersonel(ortPersonel);
                }
            }

        } catch (Exception ex) {
            log.error("Malzeme teslim güncelleme hatası", ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

}
