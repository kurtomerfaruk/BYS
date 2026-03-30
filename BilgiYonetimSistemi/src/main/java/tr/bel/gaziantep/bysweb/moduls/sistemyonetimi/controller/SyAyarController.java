package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.component.selectonebutton.SelectOneButton;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyGenelAyar;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyGenelAyarService;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyKullaniciService;

import java.io.Serial;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 25.08.2025 08:45
 */
@Named
@ViewScoped
@Slf4j
public class SyAyarController implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 8455382748135946971L;

    @Inject
    private SyKullaniciService syKullaniciService;
    @Inject
    private SyGenelAyarService syGenelAyarService;

    @Getter
    @Setter
    private String notification;

    @PostConstruct
    public void init() {
        SyGenelAyar ayar = syGenelAyarService.findByTanim("SendNotification");
        if (ayar != null) {
            notification=ayar.getDeger();
        }
    }

    public void clearCache(){
        try {
            syKullaniciService.clearCache();
            FacesUtil.successMessage("onBellekTemizlendi");
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }


    public void onNotificationChange(AjaxBehaviorEvent event) {
       try {
           Object value = ((SelectOneButton) event.getSource()).getValue();


           SyGenelAyar ayar = syGenelAyarService.findByTanim("SendNotification");
           if(ayar!=null){
               ayar.setDeger(value.toString());
               syGenelAyarService.edit(ayar);
               FacesUtil.successMessage(Constants.KAYIT_GUNCELLENDI);
           }else{
               ayar= new SyGenelAyar();
               ayar.setTanim("SendNotification");
               ayar.setDeger(value.toString());
               syGenelAyarService.create(ayar);
            FacesUtil.successMessage(Constants.KAYIT_EKLENDI);
           }
       }catch (Exception ex){
           log.error(ex.getMessage(), ex);
           FacesUtil.errorMessage(Constants.HATA_OLUSTU);
       }
    }



}
