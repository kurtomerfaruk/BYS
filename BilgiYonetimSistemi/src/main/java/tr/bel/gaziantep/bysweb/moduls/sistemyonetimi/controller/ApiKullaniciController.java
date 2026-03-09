package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.KpsController;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.webservice.api.entity.ApiKullanici;
import tr.bel.gaziantep.bysweb.webservice.api.utils.ApiKeyUtil;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.5.0
 * @since 18.02.2026 10:49
 */
@Named
@ViewScoped
@Slf4j
public class ApiKullaniciController extends AbstractController<ApiKullanici> {

    @Serial
    private static final long serialVersionUID = -379946367176064511L;

    @Inject
    private GnlKisiService gnlKisiService;
    @Inject
    private KpsController kpsController;

    public ApiKullaniciController() {
        super(ApiKullanici.class);
    }

    @Override
    public ApiKullanici prepareCreate(ActionEvent event) {
        ApiKullanici newItem;
        try {
            newItem = ApiKullanici.class.getDeclaredConstructor().newInstance();
            GnlKisi kisi = GnlKisi.builder()
                    .kayitTarihi(LocalDate.now())
                    .sosyalYardimAliyorMu(false)
                    .eklemeYeri(EnumModul.SISTEM_YONETIMI)
                    .gnlIl(new GnlIl())
                    .gnlIlce(new GnlIlce())
                    .gnlMahalle(new GnlMahalle())
                    .build();
            newItem.setGnlKisi(kisi);
            String appKey = ApiKeyUtil.generate(20);
            String appSecret = ApiKeyUtil.generate(20);
            newItem.setAppKey(appKey);
            newItem.setAppSecret(appSecret);
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
                String tcKimlikNo = this.getSelected().getGnlKisi().getTcKimlikNo();
                GnlKisi kisi = gnlKisiService.findByTckimlikNo(tcKimlikNo);
                if (kisi == null) kisi = this.getSelected().getGnlKisi();
                kisi = kpsController.findByTcKimlikNo(kisi, EnumModul.SISTEM_YONETIMI);
                if (kisi != null) {
                    this.getSelected().setGnlKisi(kisi);
                }

            }
        } catch (Exception ex) {
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
            log.error(null, ex);
        }
    }

    public void generateAppKey(){
        if(this.getSelected()==null) return;
        String appKey =  ApiKeyUtil.generate(20);
        this.getSelected().setAppKey(appKey);
    }

    public void generateAppSecret(){
        if(this.getSelected()==null) return;
        String appSecret = ApiKeyUtil.generate(20);
        this.getSelected().setAppSecret(appSecret);
    }
}
