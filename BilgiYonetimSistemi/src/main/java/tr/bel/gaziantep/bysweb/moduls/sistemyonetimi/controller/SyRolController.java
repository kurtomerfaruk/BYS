package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.entity.BaseEntity;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.*;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyRolService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyYetkiService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 16:34
 */
@Named
@ViewScoped
@Slf4j
public class SyRolController extends AbstractController<SyRol> {

    @Serial
    private static final long serialVersionUID = -827487176867291101L;

    @Inject
    private SyRolService service;
    @Inject
    private SyYetkiService yetkiService;
    @Inject
    private SyKullaniciService syKullaniciService;

    @Getter
    @Setter
    private List<SyYetki> selectedYetkis;
    private List<SyYetki> yetkiList=new ArrayList<>();
    @Getter
    @Setter
    private List<SyKullanici> selectedKullanicis;
    private List<SyKullanici> kullaniciList=new ArrayList<>();

    public SyRolController() {
        super(SyRol.class);
    }


    @Override
    public SyRol prepareCreate(ActionEvent event) {
        SyRol newItem;
        try {
            newItem = SyRol.class.getDeclaredConstructor().newInstance();
            selectedYetkis = new ArrayList<>();
            yetkiList = yetkiService.findAll();
            selectedKullanicis = new ArrayList<>();
            kullaniciList = syKullaniciService.findAll();
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void create() {
        if (this.getSelected() == null) return;
        try {
            service.update(this.getSelected(), selectedYetkis,selectedKullanicis);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void update() {
        if (this.getSelected() == null) return;
        try {
            service.update(this.getSelected(), selectedYetkis,selectedKullanicis);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        if (this.getSelected() == null) return;
        selectedYetkis =
                this.getSelected().getSyRolYetkis().stream().filter(BaseEntity::isAktif).map(SyRolYetki::getSyYetki).toList();
        yetkiList = yetkiService.findAll();

        selectedKullanicis = this.getSelected().getSyKullaniciRolList().stream().filter(BaseEntity::isAktif).map(SyKullaniciRol::getSyKullanici).toList();

        kullaniciList = syKullaniciService.findAll();
    }

    public List<SyYetki> completeYetkis(String query) {
        String queryLowerCase = query.toLowerCase();
        List<SyYetki> result = yetkiList.stream().filter(t -> t.getAciklama().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        result.removeAll(selectedYetkis);
        return result;
    }

    public void onItemUnSelect(UnselectEvent<SyYetki> event) {
        SyYetki yetki = event.getObject();
        selectedYetkis.remove(yetki);
    }

    public void onItemSelect(SelectEvent<SyYetki> event) {
        SyYetki yetki = event.getObject();
        if (!selectedYetkis.contains(yetki)) {
            selectedYetkis.add(yetki);
        }
    }

    public List<SyKullanici> completeKullanicis(String query) {
        String queryLowerCase = query.toLowerCase();
        List<SyKullanici> result = kullaniciList.stream().filter(t ->t.getKullaniciAdi().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        result.removeAll(selectedKullanicis);
        return result;
    }

    public void onItemUnSelectKullanici(UnselectEvent<SyKullanici> event) {
        SyKullanici kullanici = event.getObject();
        selectedKullanicis.remove(kullanici);
    }

    public void onItemSelectKullanici(SelectEvent<SyKullanici> event) {
        SyKullanici kullanici = event.getObject();
        if (!selectedKullanicis.contains(kullanici)) {
            selectedKullanicis.add(kullanici);
        }
    }

    public void saveAdminPermission() {
        try {
            SyRol rol = service.find(1);
            if (rol != null) {
                List<SyYetki> yetkis = yetkiService.findAll();
                for (SyRolYetki rolYetki : rol.getSyRolYetkis()) {
                    yetkis.remove(rolYetki.getSyYetki());
                }
                for (SyYetki yetki : yetkis) {
                    rol.getSyRolYetkis().add(SyRolYetki.builder()
                            .syRol(rol)
                            .syYetki(yetki)
                            .build());
                }
                service.edit(rol);
                FacesUtil.addSuccessMessage("Rol Güncellendi");
            }
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


}
