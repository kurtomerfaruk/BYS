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
import tr.bel.gaziantep.bysweb.core.enums.ErrorType;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.exception.BysBusinessException;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyEngelGrubu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiEngelGrubuService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.entity.OrtHasta;
import tr.bel.gaziantep.bysweb.moduls.ortezprotez.service.OrtHastaService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public OrtHastaController() {
        super(OrtHasta.class);
    }


    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case ORTENGEL_OLUSUM -> {
                return filterOptionService.getOrtEngelOlusums();
            }
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
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .eklemeYeri(EnumModul.ORTEZ_PROTEZ).build();
            newItem.setGnlKisi(kisi);
            this.setSelected(newItem);
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
    }

    public void getDisabledInfo() {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_SECILEMEDI);
        }

        if (this.getSelected().getGnlKisi() == null) {
            throw new BysBusinessException(ErrorType.KISI_BILGILERI_OKUNAMADI);
        }

        if (!this.getSelected().isEngelli()) {
            eyKisi = null;
            eyEngelGrubus = new ArrayList<>();
        }

        if (!StringUtil.isBlank(this.getSelected().getGnlKisi().getTcKimlikNo())) {
            eyKisi = eyKisiService.findByTcKimlikNo(this.getSelected().getGnlKisi().getTcKimlikNo());

            if (eyKisi != null) {
                eyEngelGrubus = eyKisiEngelGrubuService.getEyEngelGrubuByEyKisi(eyKisi);
                this.getSelected().setEngelli(!eyEngelGrubus.isEmpty());
            }
        }
    }

    @Override
    public void save(ActionEvent event) {
        if (this.getSelected() == null) {
            throw new BysBusinessException(ErrorType.NESNE_SECILEMEDI);
        }
        try {
            service.save(this.getSelected(), eyKisi, eyEngelGrubus);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
