package tr.bel.gaziantep.bysweb.moduls.ortezprotez.controller;

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
import tr.bel.gaziantep.bysweb.core.controller.AbstractKisiController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlYakinlikDerecesi;
import tr.bel.gaziantep.bysweb.core.enums.ortezprotez.EnumOrtCihazTur;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelinOlusumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelinOlusumu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiEngelGrubuService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtCihaz;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHastaKullandigiCihaz;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtHastaService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 24.09.2025 09:08
 */
@Named
@ViewScoped
@Slf4j
public class OrtHastaController extends AbstractKisiController<OrtHasta> {

    @Serial
    private static final long serialVersionUID = 1669461090844928292L;

    @Inject
    private OrtHastaService service;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private EyKisiEngelGrubuService eyKisiEngelGrubuService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private List<EyEngelGrubu> eyEngelGrubus = new ArrayList<>();
    @Getter
    @Setter
    private EyKisi eyKisi;
    @Getter
    @Setter
    private List<OrtCihaz> ortezCihazList;
    @Getter
    @Setter
    private List<OrtCihaz> protezCihazList;
    @Getter
    @Setter
    private List<EyEngelinOlusumu> engelinOlusumuList;
    @Getter
    @Setter
    private boolean sehirDisi;
    @Getter
    @Setter
    private List<EnumGnlYakinlikDerecesi> yakinlikDerecesiList;

    public OrtHastaController() {
        super(OrtHasta.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ILCE -> {
                return filterOptionService.getGnlIlces();
            }
            case IL -> {
                return filterOptionService.getGnlIls();
            }
            default -> {
                return Collections.emptyList();
            }
        }

    }


    @Override
    public OrtHasta prepareCreate(ActionEvent event) {
        OrtHasta newItem;
        try {
            newItem = OrtHasta.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .gnlIl(new GnlIl())
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .eklemeYeri(EnumModul.ORTEZ_PROTEZ).build();
            eyEngelGrubus = new ArrayList<>();
            newItem.setGnlKisi(kisi);
            this.setSelected(newItem);
            clearList();
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void hastaSecKapat(OrtHasta ortHasta) {
        PrimeFaces.current().dialog().closeDynamic(ortHasta);
    }

    public void onRowDblSelect(SelectEvent<OrtHasta> event) {
        OrtHasta ortHasta = event.getObject();
        hastaSecKapat(ortHasta);
    }

    public void getTcKimlik() {
//        if (this.getSelected() != null) {
//            try {
//                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
//                LocalDate dogumTarihi = this.getSelected().getGnlKisi().getDogumTarihi();
//                GnlKisi kisi = kisiService.findByTckimlikNoByDogumTarihi(tcKimlikNo, dogumTarihi);
//                if (kisi == null) kisi = this.getSelected().getGnlKisi();
//                kisi.setAktif(Boolean.TRUE);
//                kisi = kps.findByTcKimlikNo(kisi, EnumModul.ORTEZ_PROTEZ);
//                this.getSelected().setGnlKisi(kisi);
//            } catch (Exception ex) {
//                log.error(null, ex);
//                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
//            }
//        }
        fetchTcKimlik(
                OrtHasta::getGnlKisi,
                OrtHasta::setGnlKisi,
                EnumModul.ORTEZ_PROTEZ
        );

        sehirDisi = this.getSelected().getGnlKisi().getGnlIl() == null || this.getSelected().getGnlKisi().getGnlIl().getId() != 27;

        getDisabledInfo();
    }

    public void getDisabledInfo() {
        if (this.getSelected() != null && this.getSelected().getGnlKisi() != null) {
            clearList();
            try {
                eyEngelGrubus = new ArrayList<>();

                if (!StringUtil.isBlank(this.getSelected().getGnlKisi().getTcKimlikNo())) {
                    eyKisi = eyKisiService.findByTcKimlikNo(this.getSelected().getGnlKisi().getTcKimlikNo());

                    if (eyKisi != null) {
                        eyEngelGrubus = eyKisiEngelGrubuService.getEyEngelGrubuByEyKisi(eyKisi);
                        this.getSelected().setEngelli(!eyEngelGrubus.isEmpty());
                        this.getSelected().setEngelli(true);

                        engelinOlusumuList = Function.filterAndCollect(eyKisi.getEyKisiEngelinOlusumuList(), EyKisiEngelinOlusumu::isSecili,
                                EyKisiEngelinOlusumu::getEyEngelinOlusumu);

                    } else {
                        eyKisi = EyKisi.builder()
                                .gnlKisi(this.getSelected().getGnlKisi())
                                .eyKisiEngelGrubuList(new ArrayList<>())
                                .build();

                    }
                }

                List<OrtHastaKullandigiCihaz> ortezCihazs =
                        this.getSelected().getOrtHastaKullandigiCihazList().stream().filter(x -> x.getOrtCihaz().getCihazTur().equals(EnumOrtCihazTur.ORTEZ)).toList();
                ortezCihazList = Function.filterAndCollect(ortezCihazs, OrtHastaKullandigiCihaz::isSecili,
                        OrtHastaKullandigiCihaz::getOrtCihaz);
                List<OrtHastaKullandigiCihaz> protezCihazs =
                        this.getSelected().getOrtHastaKullandigiCihazList().stream().filter(x -> x.getOrtCihaz().getCihazTur().equals(EnumOrtCihazTur.PROTEZ)).toList();
                protezCihazList = Function.filterAndCollect(protezCihazs, OrtHastaKullandigiCihaz::isSecili,
                        OrtHastaKullandigiCihaz::getOrtCihaz);

                yakinlikDerecesiList = new ArrayList<>();
                String birinciDereceVefatDurumu = StringUtil.removeBracket(this.getSelected().getBirinciDereceVefatDurumu());
                if (StringUtil.isNotBlank(birinciDereceVefatDurumu)) {
                    yakinlikDerecesiList = new ArrayList<>();
                    yakinlikDerecesiList = Arrays.stream(birinciDereceVefatDurumu.split(","))
                            .map(EnumGnlYakinlikDerecesi::fromName)
                            .collect(Collectors.toList());
                }
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }

    }

    public void clearList() {
        ortezCihazList = new ArrayList<>();
        protezCihazList = new ArrayList<>();
        engelinOlusumuList = new ArrayList<>();
        yakinlikDerecesiList = new ArrayList<>();
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                if (isDuplicatePatient()) {
                    return;
                }
                generateYakinlikDerecesi();
                service.save(this.getSelected(), eyKisi, eyEngelGrubus, ortezCihazList, protezCihazList, engelinOlusumuList);
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    /**
     * Aynı TC kimlik numarasına sahip başka bir aktif hasta kaydı olup olmadığını kontrol eder.
     * Create ve Edit sayfaları ortak {@link #save} metodunu kullandığı için hem ekleme
     * hem de güncelleme sırasında mükerrer kayıt engellenir.
     */
    private boolean isDuplicatePatient() {
        if (this.getSelected() == null || this.getSelected().getGnlKisi() == null) {
            return false;
        }

        String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
        if (StringUtil.isBlank(tcKimlikNo)) {
            return false;
        }

        OrtHasta existing = service.findByTcKimlikNo(tcKimlikNo);
        if (existing != null && !Objects.equals(existing.getId(), this.getSelected().getId())) {
            FacesUtil.addErrorMessage(existing.getGnlKisi().getAdSoyad() + " T.C. kimlik numarasına ait zaten bir hasta kaydı bulunmaktadır.");
            return true;
        }

        return false;
    }

    public void getViewInfo(OrtHasta ortHasta) {
        if (ortHasta != null) {
            this.setSelected(ortHasta);
            getDisabledInfo();
            PrimeFaces.current().executeScript("PF('OrtHastaViewDialog').show()");
        }
    }

    private void generateYakinlikDerecesi() {
        String yakinlikDereceleri = EnumUtil.enumListToString(yakinlikDerecesiList);
        this.getSelected().setBirinciDereceVefatDurumu(yakinlikDereceleri);
    }


}
