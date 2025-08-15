package tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.entity.SyDuyuruKullanici;
import tr.bel.gaziantep.bysweb.moduls.sistemyonetimi.service.SyDuyuruKullaniciService;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 14.08.2025 13:08
 */
@Named
@ViewScoped
@Slf4j
public class SyDuyuruKullaniciController extends AbstractController<SyDuyuruKullanici> {

    @Serial
    private static final long serialVersionUID = 4515480709146501575L;

    @Inject
    private SyDuyuruKullaniciService service;

    public SyDuyuruKullaniciController() {
        super(SyDuyuruKullanici.class);
    }

    public void updateAnnouncement(SyDuyuruKullanici syDuyuruKullanici) {
        if (syDuyuruKullanici != null) {
            try {
                syDuyuruKullanici.setOkunmaTarihi(LocalDateTime.now());
                syDuyuruKullanici.setOkundu(true);
                service.edit(syDuyuruKullanici);
                FacesUtil.successMessage("duyuruOkundu");
            } catch (Exception ex) {
                log.error(null,ex);
                FacesUtil.errorMessage("kayitOnaylanirkenHata");
            }
        }
    }
}
