package tr.bel.gaziantep.bysweb.core.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.converter.ModelConverter;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KpsService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.util.KpsUtil;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:36
 */
@Named
@RequestScoped
@Slf4j
public class KpsController implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 7077919370744153089L;

    @Inject
    private InitApp initApp;
    @Inject
    private ModelConverter converter;

    public GnlKisi findByTcKimlikNo(GnlKisi kisi,  EnumModul modul) throws Exception {
        String tcKimlikNo = kisi.getTcKimlikNo();

        KisiParameter parameter = KpsUtil.setDate(kisi.getDogumTarihi());
        parameter.setTcKimlikNo(Long.parseLong(tcKimlikNo));
        KpsService kpsService = new KpsService();
        KpsModel kpsModel = kpsService.kpsFullSorgula(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameter);

        if (kpsModel == null) return null;

        if (!StringUtil.isBlank(kpsModel.getKutukModel().getHataBilgisi())) {
            FacesUtil.addErrorMessage(kpsModel.getKutukModel().getHataBilgisi());
            return null;
        }
        kisi = converter.convertKpsModelToGnlKisi(kisi, kpsModel, modul);
        kisi.setMernisGuncellemeTarihi(LocalDateTime.now());
        return kisi;
    }
}
