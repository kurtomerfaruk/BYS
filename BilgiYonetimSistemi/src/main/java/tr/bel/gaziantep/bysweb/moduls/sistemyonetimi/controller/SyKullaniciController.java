package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.model.SelectItem;
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
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyFiltreAnahtari;
import tr.bel.gaziantep.bysweb.core.enums.sistemyonetimi.EnumSyKullaniciTuru;
import tr.bel.gaziantep.bysweb.core.service.FilterOptionService;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.Function;
import tr.bel.gaziantep.bysweb.core.validation.StrongPassword;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlPersonel;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlUnvan;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyKullaniciRol;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyRol;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyRolService;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 18.06.2025 10:42
 */
@Named
@ViewScoped
@Slf4j
public class SyKullaniciController extends AbstractController<SyKullanici> {

    @Serial
    private static final long serialVersionUID = -325363333052546835L;

    @Inject
    private SyKullaniciService service;
    @Inject
    private SyRolService syRolService;
    @Inject
    private FilterOptionService filterOptionService;

    @Getter
    @Setter
    private Set<String> permissions = new LinkedHashSet<>();
    @Getter
    @Setter
    private List<SyRol> selectedRols;
    @Getter
    @Setter
    private String currentPassword;
    @Getter
    @Setter
    @StrongPassword
    private String newPassword;
    @Getter
    @Setter
    private String newPasswordRepeat;
    @Getter
    @Setter
    private boolean changePassword;

    public SyKullaniciController() {
        super(SyKullanici.class);
    }

    public List<SelectItem> getFilterOptions(EnumSyFiltreAnahtari key) {
        switch (key) {
            case KULLANICI_TURU -> {
                return filterOptionService.getSyKullaniciTurus();
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

    @Override
    @PostConstruct
    public void init() {
//        syKullanici = Util.getSyKullanici();
//        readColumns(syKullanici);
        this.getSyKullanici().getSyKullaniciRols().stream()
                .flatMap(kullaniciRol -> kullaniciRol.getSyRol().getSyRolYetkis().stream().filter(BaseEntity::isAktif))
                .map(rolYetki -> rolYetki.getSyYetki().getYetki())
                .forEach(permissions::add);
        selectedRols = new ArrayList<>();
    }

    public boolean hasPermission(String permissionKey) {
        return permissions.contains(permissionKey);
    }

    @Override
    public SyKullanici prepareCreate(ActionEvent event) {
        SyKullanici newItem;
        try {
            newItem = SyKullanici.class.getDeclaredConstructor().newInstance();
            newItem.setGnlPersonel(GnlPersonel.builder()
                    .gnlKisi(new GnlKisi())
                    .gnlUnvan(new GnlUnvan())
                    .build());
            newItem.setKullaniciTuru(EnumSyKullaniciTuru.KULLANICI);
            this.setSelected(newItem);
            initializeEmbeddableKey();
            return newItem;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            log.error(null,ex);
        }
        return null;
    }

    public void secilenPersonel(SelectEvent<GnlPersonel> event) {
        GnlPersonel personel = event.getObject();
        this.getSelected().setGnlPersonel(personel);
    }

    public void create() {
        if (this.getSelected() == null) return;
        try {
            this.getSelected().setParola(Function.encrypt(this.getSelected().getParola()));
            service.update(this.getSelected(), selectedRols);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void update() {
        if (this.getSelected() == null) return;
        try {
            service.update(this.getSelected(), selectedRols);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            log.error(null, ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }

    public void readInfo() {
        if (this.getSelected() == null) return;
        selectedRols = this.getSelected()
                .getSyKullaniciRols()
                .parallelStream()
                .filter(BaseEntity::isAktif)
                .map(SyKullaniciRol::getSyRol)
                .collect(Collectors.toList());
    }

    public void allPasswordChange() {
        try {
            List<SyKullanici> kullaniciList = service.findAll();
            kullaniciList.forEach(k -> k.setParolaDegistirilsin(true));
            service.editAll(kullaniciList);
            FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null,ex);
        }
    }

    public void passwordUpdate(ActionEvent event) {
        if (this.getSelected() != null) {
            this.getSelected().setParolaDegistirilsin(changePassword);
            this.getSelected().setParola(Function.encrypt(newPassword));
            this.save(event);
        }
    }

    public List<SyRol> completeRols(String query) {
        String queryLowerCase = query.toLowerCase();
        List<SyRol> rols = syRolService.findAll();
        List<SyRol> result = rols.stream().filter(t -> t.getAciklama().toLowerCase().contains(queryLowerCase)).collect(Collectors.toList());
        result.removeAll(selectedRols);
        return result;
    }

    public void onItemUnSelect(UnselectEvent<SyRol> event) {
        SyRol rol = event.getObject();
        selectedRols.remove(rol);
    }

    public void onItemSelect(SelectEvent<SyRol> event) {
        SyRol rol = event.getObject();
        if (!selectedRols.contains(rol)) {
            selectedRols.add(rol);
        }
    }

    public void profilePasswordUpdate(ActionEvent event) {
        try {
            if (this.getSyKullanici() != null) {
                if (!this.getSyKullanici().getParola().equals(Function.encrypt(currentPassword))) {
                    FacesUtil.warningMessage("parolaHatali");
                    return;
                }
                if(!Function.isPasswordStrong(newPassword)){
                    FacesUtil.warningMessage("gucluParolaKontrol");
                    return;
                }
                this.getSyKullanici().setParola(Function.encrypt(newPassword));
                this.setSelected(this.getSyKullanici());
                this.save(event);
                currentPassword = "";
                newPassword = "";
                newPasswordRepeat = "";
            }
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
