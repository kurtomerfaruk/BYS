package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.utils.Constants;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
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

    public void clearCache(){
        try {
            syKullaniciService.clearCache();
            FacesUtil.successMessage("onBellekTemizlendi");
        }catch (Exception ex){
            log.error(ex.getMessage(), ex);
            FacesUtil.errorMessage(Constants.HATA_OLUSTU);
        }
    }
}
