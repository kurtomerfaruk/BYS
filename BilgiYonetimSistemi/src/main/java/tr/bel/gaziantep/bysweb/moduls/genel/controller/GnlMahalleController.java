package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;
import org.omnifaces.cdi.Push;
import org.omnifaces.cdi.PushContext;
import org.primefaces.PrimeFaces;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlceService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlMahalleService;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.MahalleSorgulaService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.mahalle.MahalleSorguSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KodParameter;

import java.io.Serial;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 30.06.2025 09:04
 */
@Named
@ViewScoped
@Slf4j
public class GnlMahalleController extends AbstractController<GnlMahalle> {

    @Serial
    private static final long serialVersionUID = -4090520882386659283L;

    @Inject
    private GnlMahalleService service;
    @Inject
    private GnlIlceService gnlIlceService;
    @Inject
    private InitApp initApp;
    @Inject
    @Push(channel = "mahalleChannel")
    private PushContext push;

    public GnlMahalleController() {
        super(GnlMahalle.class);
    }

    public List<GnlMahalle> getMahalleByIlce(GnlIlce gnlIlce){
        return service.findByIlce(gnlIlce);
    }

    public void updateList() {
        try {
            List<GnlIlce> ilces = gnlIlceService.findByIlId(27);
            MahalleSorgulaService sorgulaService = new MahalleSorgulaService();
            for (GnlIlce ilce : ilces) {
                KodParameter parameter = new KodParameter();
                parameter.setKod(ilce.getIlceKodu().longValue());
                List<MahalleSorguSonucu> mahalleList = sorgulaService.ilceyeBagliMahalleSorgula(initApp.getProperty("webServisLink"), initApp.getProperty("webServisToken"), parameter);
                for (MahalleSorguSonucu mahalleModel : mahalleList) {
                    GnlMahalle mahalle = service.findByKod(mahalleModel.getKod());
                    if (mahalle == null) {
                        mahalle = GnlMahalle.builder().kod(mahalleModel.getKod()).tanim(mahalleModel.getAciklama()).build();
                    }

                    mahalle.setGnlIlce(ilce);
                    mahalle.setAktif(true);
                    service.merge(mahalle);
                    String tanim = ilce.getTanim() + "," + mahalle.getTanim();
                    push.send(tanim);
                }
            }
            FacesUtil.successMessage("listeGuncellendi");
            PrimeFaces.current().ajax().update(":GnlMahalleListForm:datalist:datalist", ":growl");
        } catch (Exception ex) {
            log.error(null,ex);
            FacesUtil.errorMessage("mahalleGuncellemeHata");
        } finally {
            PrimeFaces.current().executeScript("PF('MahalleGuncelle').hide()");
        }
    }
}
