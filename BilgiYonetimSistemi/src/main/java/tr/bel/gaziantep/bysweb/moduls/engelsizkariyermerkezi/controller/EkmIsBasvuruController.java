package tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmIsBasvuruService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisiEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 4.07.2025 11:17
 */
@Named
@ViewScoped
@Slf4j
public class EkmIsBasvuruController extends AbstractController<EkmIsBasvuru> {

    @Serial
    private static final long serialVersionUID = -7106491619144089971L;

    @Inject
    private EkmIsBasvuruService service;
    @Inject
    private GnlKisiService kisiService;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private KpsController kps;

    @Getter
    @Setter
    private EkmIsBasvuruEgitim ekmIsBasvuruEgitim;
    @Getter
    @Setter
    private EkmIsBasvuruYabanciDil ekmIsBasvuruYabanciDil;
    @Getter
    @Setter
    private EkmIsBasvuruKullandigiProgram ekmIsBasvuruKullandigiProgram;
    @Getter
    @Setter
    private EkmIsBasvuruTecrube ekmIsBasvuruTecrube;
    @Getter
    @Setter
    private EkmIsBasvuruKursSeminer ekmIsBasvuruKursSeminer;
    @Getter
    @Setter
    private EkmIsBasvuruReferans ekmIsBasvuruReferans;
    @Getter
    @Setter
    private List<EyEngelGrubu> engelGrubus;

    public EkmIsBasvuruController() {
        super(EkmIsBasvuru.class);
    }

    @Override
    public EkmIsBasvuru prepareCreate(ActionEvent event) {
        EkmIsBasvuru newItem;
        try {
            createTempObject();
            newItem = EkmIsBasvuru.class.getDeclaredConstructor().newInstance();
            newItem.setBasvuruTarihi(LocalDateTime.now());
            newItem.setEyKisi(EyKisi.builder().gnlKisi(new GnlKisi()).build());
            newItem.setEkmIsBasvuruEgitimList(new ArrayList<>());
            newItem.setEkmIsBasvuruYabanciDilList(new ArrayList<>());
            newItem.setEkmIsBasvuruTecrubeList(new ArrayList<>());
            newItem.setEkmIsBasvuruKullandigiProgramList(new ArrayList<>());
            newItem.setEkmIsBasvuruKursSeminerList(new ArrayList<>());
            newItem.setEkmIsBasvuruReferansList(new ArrayList<>());
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    private void createTempObject() {
        ekmIsBasvuruEgitim = new EkmIsBasvuruEgitim();
        ekmIsBasvuruYabanciDil = new EkmIsBasvuruYabanciDil();
        ekmIsBasvuruKullandigiProgram = new EkmIsBasvuruKullandigiProgram();
        ekmIsBasvuruTecrube = new EkmIsBasvuruTecrube();
        ekmIsBasvuruKursSeminer = new EkmIsBasvuruKursSeminer();
        ekmIsBasvuruReferans = new EkmIsBasvuruReferans();
        engelGrubus = new ArrayList<>();
    }

    public void create(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                service.create(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void update(ActionEvent event) {
        if (this.getSelected() != null) {
            try {
                service.edit(this.getSelected());
                FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }


    private <T> T addItem(List<T> itemList, T newItem, Predicate<T> filterCondition, Consumer<T> setRelation, Supplier<T> createNewItem) {
        try {
            boolean itemExists = itemList.stream()
                    .anyMatch(filterCondition);

            if (!itemExists) {
                setRelation.accept(newItem);
                itemList.add(newItem);
                FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
                return createNewItem.get();
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
        return newItem;
    }

    private <T> void removeItem(List<T> itemList, T itemToRemove) {
        try {
            if (itemList.remove(itemToRemove)) {
                FacesUtil.successMessage(Constants.KAYIT_SILINDI);
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void addEducation() {
        ekmIsBasvuruEgitim = addItem(
                this.getSelected().getEkmIsBasvuruEgitimList(),
                ekmIsBasvuruEgitim,
                basvuruEgitim -> basvuruEgitim.getId() != null && !basvuruEgitim.getId().equals(ekmIsBasvuruEgitim.getId()),
                egitim -> egitim.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruEgitim::new
        );
    }

    public void removeEducation(EkmIsBasvuruEgitim egitim) {
        removeItem(this.getSelected().getEkmIsBasvuruEgitimList(), egitim);
    }

    public void addExperience() {
        ekmIsBasvuruTecrube = addItem(
                this.getSelected().getEkmIsBasvuruTecrubeList(),
                ekmIsBasvuruTecrube,
                basvuruTecrube -> basvuruTecrube.getId() != null && !basvuruTecrube.getId().equals(ekmIsBasvuruTecrube.getId()),
                tecrube -> tecrube.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruTecrube::new
        );
    }

    public void removeExperience(EkmIsBasvuruTecrube tecrube) {
        removeItem(this.getSelected().getEkmIsBasvuruTecrubeList(), tecrube);
    }

    public void addProgram() {
        ekmIsBasvuruKullandigiProgram = addItem(
                this.getSelected().getEkmIsBasvuruKullandigiProgramList(),
                ekmIsBasvuruKullandigiProgram,
                basvuruProgram -> basvuruProgram.getId() != null && !basvuruProgram.getId().equals(ekmIsBasvuruKullandigiProgram.getId()),
                program -> program.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruKullandigiProgram::new
        );
    }

    public void removeProgram(EkmIsBasvuruKullandigiProgram program) {
        removeItem(this.getSelected().getEkmIsBasvuruKullandigiProgramList(), program);
    }

    public void addCourse() {
        ekmIsBasvuruKursSeminer = addItem(
                this.getSelected().getEkmIsBasvuruKursSeminerList(),
                ekmIsBasvuruKursSeminer,
                basvuruKurs -> basvuruKurs.getId() != null && !basvuruKurs.getId().equals(ekmIsBasvuruKursSeminer.getId()),
                kurs -> kurs.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruKursSeminer::new
        );
    }

    public void removeCourse(EkmIsBasvuruKursSeminer kurs) {
        removeItem(this.getSelected().getEkmIsBasvuruKursSeminerList(), kurs);
    }

    public void addReference() {
        ekmIsBasvuruReferans = addItem(
                this.getSelected().getEkmIsBasvuruReferansList(),
                ekmIsBasvuruReferans,
                basvuruReferans -> basvuruReferans.getId() != null && !basvuruReferans.getId().equals(ekmIsBasvuruReferans.getId()),
                referans -> referans.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruReferans::new
        );
    }

    public void removeReference(EkmIsBasvuruReferans tecrube) {
        removeItem(this.getSelected().getEkmIsBasvuruReferansList(), tecrube);
    }

    public void addLanguage() {
        ekmIsBasvuruYabanciDil = addItem(
                this.getSelected().getEkmIsBasvuruYabanciDilList(),
                ekmIsBasvuruYabanciDil,
                basvuruDil -> basvuruDil.getId() != null && !basvuruDil.getId().equals(ekmIsBasvuruYabanciDil.getId()),
                dil -> dil.setEkmIsBasvuru(this.getSelected()),
                EkmIsBasvuruYabanciDil::new
        );
    }

    public void removeLanguage(EkmIsBasvuruYabanciDil dil) {
        removeItem(this.getSelected().getEkmIsBasvuruYabanciDilList(), dil);
    }

    public void getTcKimlik() {
        if (this.getSelected() != null) {
            try {
                String tcKimlikNo = this.getSelected().getEyKisi().getGnlKisi().getTcKimlikNo();

                EyKisi eyKisi = eyKisiService.findByTcKimlikNo(tcKimlikNo);
                if (eyKisi == null) {
                    GnlKisi kisi = kisiService.findByTckimlikNo(tcKimlikNo);
                    if (kisi == null) {
                        kisi = GnlKisi.builder().tcKimlikNo(tcKimlikNo).dogumTarihi(this.getSelected().getEyKisi().getGnlKisi().getDogumTarihi()).build();
                    }
                    kisi = kps.findByTcKimlikNo(kisi, EnumModul.EKM);
                    eyKisi = EyKisi.builder().gnlKisi(kisi).build();
                } else {
                    eyKisi.getEyKisiEngelGrubuList()
                            .stream()
                            .filter(EyKisiEngelGrubu::isSecili)
                            .forEach(eyKisiEngelgrubu -> engelGrubus.add(eyKisiEngelgrubu.getEyEngelGrubu()));
                }

                this.getSelected().setEyKisi(eyKisi);
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            }
        }
    }

    public void getInfo() {
        if (this.getSelected() != null) {
            createTempObject();
            this.getSelected().getEyKisi().getEyKisiEngelGrubuList()
                    .stream().filter(EyKisiEngelGrubu::isSecili)
                    .forEach(eyKisiEngelgrubu -> engelGrubus.add(eyKisiEngelgrubu.getEyEngelGrubu()));
        }
    }
}
