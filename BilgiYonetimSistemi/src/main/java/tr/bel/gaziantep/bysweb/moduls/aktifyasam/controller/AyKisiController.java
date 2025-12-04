package tr.bel.gaziantep.bysweb.moduls.aktifyasam.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.event.ValueChangeEvent;
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
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.converter.ModelConverter;
import tr.bel.gaziantep.bysweb.core.enums.aktifyasam.EnumAyGrup;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlGun;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.*;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
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
 * @since 2.07.2025 10:54
 */
@Named
@ViewScoped
@Slf4j
public class AyKisiController extends AbstractController<AyKisi> {

    @Serial
    private static final long serialVersionUID = 8127490693378373296L;

    @Inject
    private AyKisiService service;
    @Inject
    private GnlKisiService kisiService;
    @Inject
    private KpsController kps;
    @Inject
    private ModelConverter converter;
    @Inject
    @Push(channel = "ayKisiChannel")
    private PushContext push;
    @Inject
    private FilterOptionService filterOptionService;

    private int count;
    @Getter
    @Setter
    private int recordCount;
    @Getter
    @Setter
    private List<AyAktivite> ayAktivites;
    @Getter
    @Setter
    private List<AySanatsalBeceri> aySanatsalBeceris;
    @Getter
    @Setter
    private List<AySaglikBilgi> aySaglikBilgis;
    @Getter
    @Setter
    private AyKisiAranacakKisi ayKisiAranacakKisi;
    private String post;
    @Getter
    @Setter
    private List<EnumGnlGun> guns;
    @Getter
    @Setter
    private List<EnumAyGrup> grups;


    public AyKisiController() {
        super(AyKisi.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case AYDEVAM_DURUMU -> {
                return filterOptionService.getAyDevamDurumus();
            }
            case AYBIRIM -> {
                return filterOptionService.getAyBirims();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }

    @Override
    public AyKisi prepareCreate(ActionEvent event) {
        AyKisi newItem;
        try {
            ayAktivites = new ArrayList<>();
            aySanatsalBeceris = new ArrayList<>();
            aySaglikBilgis = new ArrayList<>();
            ayKisiAranacakKisi = new AyKisiAranacakKisi();
            newItem = AyKisi.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .eklemeYeri(EnumModul.AKTIF_YASAM).build();
            newItem.setGnlKisi(kisi);
            newItem.setKayitTarihi(LocalDateTime.now());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void updateAdress() {
        if (recordCount > 0) {
            count = 0;
            try {
                List<AyKisi> kisiList = service.findByLatLngIsNull(recordCount);

                for (AyKisi ayKisi : kisiList) {
                    String coordinate = converter.addLatLng(ayKisi.getGnlKisi().getBinaNo());
                    if (StringUtil.isNotBlank(coordinate) && !coordinate.equals("null,null")) {
                        ayKisi.getGnlKisi().setLatLng(coordinate);
                        kisiService.updateLatLng(ayKisi.getGnlKisi());
                        count++;
                        post = ayKisi.getGnlKisi().getAdSoyad() + "," + ayKisi.getGnlKisi().getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        push.send(post);
                    } else {
                        count++;
                        post =
                                "Uyarı :Adres Bulunamadı" + "," + ayKisi.getGnlKisi().getAdSoyad() + "-" + ayKisi.getGnlKisi().getTcKimlikNo() + "," + count + "," + recordCount + ", ";
                        push.send(post);
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

    public void addCalledPerson() {
        try {
            if (!this.getSelected().getAyAranacakKisiList().contains(ayKisiAranacakKisi)) {
                ayKisiAranacakKisi.setAyKisi(this.getSelected());

                this.getSelected().getAyAranacakKisiList().add(ayKisiAranacakKisi);
                ayKisiAranacakKisi = new AyKisiAranacakKisi();
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void removeCalledPerson(AyKisiAranacakKisi kisi) {
        try {
            if (this.getSelected().getAyAranacakKisiList().contains(kisi)) {
                this.getSelected().getAyAranacakKisiList().remove(kisi);
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void update(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                service.update(this.getSelected(), ayAktivites, aySanatsalBeceris, aySaglikBilgis, grups, guns);
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }


    public void getTcKimlik() {
        if (this.getSelected() != null) {
            try {
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
                LocalDate dogumTarihi = this.getSelected().getGnlKisi().getDogumTarihi();
                GnlKisi kisi = kisiService.findByTckimlikNoByDogumTarihi(tcKimlikNo, dogumTarihi);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi.setAktif(Boolean.TRUE);
                kisi = kps.findByTcKimlikNo(kisi, EnumModul.AKTIF_YASAM);
                this.getSelected().setGnlKisi(kisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void valueChangeSaglikBilgi(ValueChangeEvent event) {
        List<AySaglikBilgi> saglikBilgileris = (List<AySaglikBilgi>) event.getNewValue();
        this.getSelected().getAyKisiSaglikBilgileriList().forEach(kisiSaglikBilgileri -> kisiSaglikBilgileri.setSecili(false));

        for (AySaglikBilgi bilgileris : saglikBilgileris) {
            AyKisiSaglikBilgi kisiSaglikBilgileri = this.getSelected().getAyKisiSaglikBilgileriList()
                    .stream()
                    .filter(ayKisiSaglikBilgileri -> ayKisiSaglikBilgileri.getAySaglikBilgi().getId().equals(bilgileris.getId()))
                    .findFirst()
                    .orElse(null);
            if (kisiSaglikBilgileri == null) {
                kisiSaglikBilgileri = AyKisiSaglikBilgi.builder().secili(true).aySaglikBilgi(bilgileris).ayKisi(this.getSelected()).build();
                this.getSelected().getAyKisiSaglikBilgileriList().add(kisiSaglikBilgileri);
            } else {
                int index = this.getSelected().getAyKisiSaglikBilgileriList().indexOf(kisiSaglikBilgileri);
                kisiSaglikBilgileri.setSecili(true);
                this.getSelected().getAyKisiSaglikBilgileriList().set(index, kisiSaglikBilgileri);
            }
        }
    }

    public void valueChangeAktivite(ValueChangeEvent event) {
        List<AyAktivite> aktivites = (List<AyAktivite>) event.getNewValue();
        this.getSelected().getAyKisiAktiviteList().forEach(kisiAktivite -> kisiAktivite.setSecili(false));

        for (AyAktivite aktivite : aktivites) {
            AyKisiAktivite ayKisiAktivite = this.getSelected().getAyKisiAktiviteList()
                    .stream()
                    .filter(kisiAktivite -> kisiAktivite.getAyAktivite().getId().equals(aktivite.getId()))
                    .findFirst()
                    .orElse(null);
            if (ayKisiAktivite == null) {
                ayKisiAktivite = AyKisiAktivite.builder().secili(true).ayKisi(this.getSelected()).ayAktivite(aktivite).build();
                this.getSelected().getAyKisiAktiviteList().add(ayKisiAktivite);
            } else {
                int index = this.getSelected().getAyKisiAktiviteList().indexOf(ayKisiAktivite);
                ayKisiAktivite.setSecili(true);
                this.getSelected().getAyKisiAktiviteList().set(index, ayKisiAktivite);
            }
        }
    }

    public void valueChangeSanatsalBeceri(ValueChangeEvent event) {
        List<AySanatsalBeceri> aySanatsalBeceris = (List<AySanatsalBeceri>) event.getNewValue();
        this.getSelected().getAyKisiSanatsalBeceriList().forEach(kisiSanatsalBeceri -> kisiSanatsalBeceri.setSecili(false));

        for (AySanatsalBeceri sanatsalBeceris : aySanatsalBeceris) {
            AyKisiSanatsalBeceri ayKisiSanatsalBeceri = this.getSelected().getAyKisiSanatsalBeceriList()
                    .stream()
                    .filter(kisiSanatsalBeceri -> kisiSanatsalBeceri.getAySanatsalBeceri().getId().equals(sanatsalBeceris.getId()))
                    .findFirst()
                    .orElse(null);
            if (ayKisiSanatsalBeceri == null) {
                ayKisiSanatsalBeceri = AyKisiSanatsalBeceri.builder().secili(true).aySanatsalBeceri(sanatsalBeceris).ayKisi(this.getSelected()).build();
                this.getSelected().getAyKisiSanatsalBeceriList().add(ayKisiSanatsalBeceri);
            } else {
                int index = this.getSelected().getAyKisiSanatsalBeceriList().indexOf(ayKisiSanatsalBeceri);
                ayKisiSanatsalBeceri.setSecili(true);
                this.getSelected().getAyKisiSanatsalBeceriList().set(index, ayKisiSanatsalBeceri);
            }
        }
    }

    public void getInfo() {
        if (this.getSelected() != null) {
            ayKisiAranacakKisi = new AyKisiAranacakKisi();
            ayAktivites = new ArrayList<>();
            aySanatsalBeceris = new ArrayList<>();
            aySaglikBilgis = new ArrayList<>();
            guns = new ArrayList<>();
            grups = new ArrayList<>();
            this.getSelected().getAyKisiSaglikBilgileriList()
                    .stream()
                    .filter(kisiSaglikBilgileri -> kisiSaglikBilgileri.isSecili() && kisiSaglikBilgileri.isAktif())
                    .forEach(kisiSaglikBilgileri -> aySaglikBilgis.add(kisiSaglikBilgileri.getAySaglikBilgi()));
            this.getSelected().getAyKisiSanatsalBeceriList()
                    .stream()
                    .filter(kisiSanatsalBeceri -> kisiSanatsalBeceri.isSecili() && kisiSanatsalBeceri.isAktif())
                    .forEach(kisiSanatsalBeceri -> aySanatsalBeceris.add(kisiSanatsalBeceri.getAySanatsalBeceri()));
            this.getSelected().getAyKisiAktiviteList()
                    .stream()
                    .filter(kisiAktivite -> kisiAktivite.isSecili() && kisiAktivite.isAktif())
                    .forEach(kisiAktivite -> ayAktivites.add(kisiAktivite.getAyAktivite()));
            this.getSelected().getAyKisiGrupList()
                    .stream()
                    .filter(grup -> grup.isSecili() && grup.isAktif())
                    .forEach(grup -> grups.add(grup.getGrup()));
            this.getSelected().getAyKisiGunList()
                    .stream()
                    .filter(gun -> gun.isSecili() && gun.isAktif())
                    .forEach(gun -> guns.add(gun.getGun()));
        }
    }

    public void ayKisiSecKapat(AyKisi ayKisi) {
        PrimeFaces.current().dialog().closeDynamic(ayKisi);
    }

    public void onRowDblSelect(SelectEvent<AyKisi> event) {
        AyKisi ayKisi = event.getObject();
        ayKisiSecKapat(ayKisi);
    }

}
