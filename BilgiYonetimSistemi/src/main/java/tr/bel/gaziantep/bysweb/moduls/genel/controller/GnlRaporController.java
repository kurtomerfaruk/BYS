package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRapor;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporEntityBaglanti;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporKolon;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlRaporParametre;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporEntityBaglantiService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporKolonService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlRaporParametreService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 16.12.2025 09:05
 */
@Named
@ViewScoped
@Slf4j
public class GnlRaporController extends AbstractController<GnlRapor> {

    @Serial
    private static final long serialVersionUID = 2192180180786528669L;


    @Inject
    private GnlRaporKolonService gnlRaporKolonService;
    @Inject
    private GnlRaporParametreService gnlRaporParametreService;
    @Inject
    private GnlRaporEntityBaglantiService gnlRaporEntityBaglantiService;

    @Getter
    @Setter
    private boolean skip = false;
    @Getter
    @Setter
    private GnlRaporKolon selectedKolon;
    @Getter
    @Setter
    private GnlRaporParametre selectedParametre;
    @Getter
    @Setter
    private GnlRaporEntityBaglanti selectedEntity;

    public GnlRaporController() {
        super(GnlRapor.class);
    }

    public GnlRaporKolon prepareCreateKolon(ActionEvent event) {
        GnlRaporKolon newItem;
        try {
            newItem = GnlRaporKolon.class.getDeclaredConstructor().newInstance();
            newItem.setSiralama(this.getSelected().getGnlRaporKolonList().size() + 1);
            this.setSelectedKolon(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void editKolon() {
        try {
            if (this.getSelected() != null) {
                if (this.getSelectedKolon().getId() == null) {
                    this.getSelectedKolon().setGnlRapor(this.getSelected());
                    this.getSelectedKolon().setAlanAdi(this.getSelectedKolon().getKolonAdi());
                    gnlRaporKolonService.create(this.getSelectedKolon());
                    this.getSelected().getGnlRaporKolonList().add(this.getSelectedKolon());
                    FacesUtil.addSuccessMessage("Kolon Eklendi");
                } else {
                    gnlRaporKolonService.edit(this.getSelectedKolon());
                    int index = this.getSelected().getGnlRaporKolonList().indexOf(this.getSelectedKolon());
                    this.getSelectedKolon().setAlanAdi(this.getSelectedKolon().getKolonAdi());
                    this.getSelected().getGnlRaporKolonList().set(index, this.getSelectedKolon());
                    FacesUtil.addSuccessMessage("Kolon Güncellendi");
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.addErrorMessage("Kolon güncellenirken hata oluştu:" + ex.getMessage());
        }
    }

    public void deleteKolon() {
        if (this.getSelected() != null && this.getSelectedKolon() != null) {
            try {
                this.getSelectedKolon().setAktif(false);
                gnlRaporKolonService.edit(this.getSelectedKolon());
                this.getSelected().getGnlRaporKolonList().remove(this.getSelectedKolon());
                FacesUtil.addSuccessMessage("Kolon silindi");
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.addErrorMessage("Kolon silinirken hata oluştu:" + ex.getMessage());
            }
        }
    }

    public GnlRaporParametre prepareCreateParameter(ActionEvent event) {
        GnlRaporParametre newItem;
        try {
            newItem = GnlRaporParametre.class.getDeclaredConstructor().newInstance();
            newItem.setSiralama(this.getSelected().getGnlRaporParametreList().size() + 1);
            this.setSelectedParametre(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void editParameter() {
        try {
            if (this.getSelected() != null) {
                if (this.getSelectedParametre().getId() == null) {
                    this.getSelectedParametre().setGnlRapor(this.getSelected());
                    gnlRaporParametreService.create(this.getSelectedParametre());
                    this.getSelected().getGnlRaporParametreList().add(this.getSelectedParametre());
                    FacesUtil.addSuccessMessage("Parametre Eklendi");
                } else {
                    gnlRaporParametreService.edit(this.getSelectedParametre());
                    int index = this.getSelected().getGnlRaporParametreList().indexOf(this.getSelectedParametre());
                    this.getSelected().getGnlRaporParametreList().set(index, this.getSelectedParametre());
                    FacesUtil.addSuccessMessage("Parametre Güncellendi");
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.addErrorMessage("Parametre güncellenirken hata oluştu:" + ex.getMessage());
        }
    }

    public void deleteParameter() {
        if (this.getSelected() != null && this.getSelectedParametre() != null) {
            try {
                this.getSelectedParametre().setAktif(false);
                gnlRaporParametreService.edit(this.getSelectedParametre());
                this.getSelected().getGnlRaporParametreList().remove(this.getSelectedParametre());
                FacesUtil.addSuccessMessage("Parametre silindi");
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.addErrorMessage("Parametre silinirken hata oluştu:" + ex.getMessage());
            }
        }
    }

    public GnlRaporEntityBaglanti prepareCreateEntity(ActionEvent event) {
        GnlRaporEntityBaglanti newItem;
        try {
            newItem = GnlRaporEntityBaglanti.class.getDeclaredConstructor().newInstance();
            newItem.setSiralama(this.getSelected().getGnlRaporEntityBaglantiList().size() + 1);
            this.setSelectedEntity(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null, ex);
        }
        return null;
    }

    public void editEntity() {
        try {
            if (this.getSelected() != null) {
                if (this.getSelectedEntity().getId() == null) {
                    this.getSelectedEntity().setGnlRapor(this.getSelected());
                    gnlRaporEntityBaglantiService.create(this.getSelectedEntity());
                    this.getSelected().getGnlRaporEntityBaglantiList().add(this.getSelectedEntity());
                    FacesUtil.addSuccessMessage("Entity Eklendi");
                } else {
                    gnlRaporEntityBaglantiService.edit(this.getSelectedEntity());
                    int index = this.getSelected().getGnlRaporEntityBaglantiList().indexOf(this.getSelectedEntity());
                    this.getSelected().getGnlRaporEntityBaglantiList().set(index, this.getSelectedEntity());
                    FacesUtil.addSuccessMessage("Entity Güncellendi");
                }
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.addErrorMessage("Entity güncellenirken hata oluştu:" + ex.getMessage());
        }
    }

    public void deleteEntity() {
        if (this.getSelected() != null && this.getSelectedEntity() != null) {
            try {
                this.getSelectedEntity().setAktif(false);
                gnlRaporEntityBaglantiService.edit(this.getSelectedEntity());
                this.getSelected().getGnlRaporEntityBaglantiList().remove(this.getSelectedEntity());
                FacesUtil.addSuccessMessage("Entity silindi");
            } catch (Exception ex) {
                log.error(null, ex);
                FacesUtil.addErrorMessage("Entity silinirken hata oluştu:" + ex.getMessage());
            }
        }
    }

    public void onEntityClassChange() {
        if (this.getSelected() != null && selectedEntity != null) {
            String entityName = selectedEntity.getEntityClass().substring(selectedEntity.getEntityClass().lastIndexOf(".") + 1);
            selectedEntity.setEntityAdi(entityName);
        }

    }

}
