package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.adres.AdresModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.BilesikKutukModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 15:43
 */
public class KpsService {

    private final BilesikKutukSorgulaService kutukServis;
    private final AdresSorgulaService adresSorgulaServis;
    private final KisiAdresSorgulaService kisiAdresSorgulaServis;

    public KpsService() {
        kutukServis = new BilesikKutukSorgulaService();
        adresSorgulaServis = new AdresSorgulaService();
        kisiAdresSorgulaServis = new KisiAdresSorgulaService();
    }


    public KpsModel kpsFullSorgula(String link, String token, KisiParameter parameter) {
        KpsModel model = new KpsModel();
        BilesikKutukModel kutukModel = this.kutukServis.tcKimlikNoSorgula(link, token, parameter);
        if (kutukModel == null) return null;
        model.setKutukModel(kutukModel);
        if (StringUtil.isNotBlank(model.getKutukModel().getHataBilgisi())) {
            return model;
        }
        model.setAdresModel(this.adresSorgulaServis.adresSorgula(link, token, parameter));
        return model;
    }

    public List<KpsModel> getKpsByKutukByAdres(String link, String token, KisiParameters parameters) {
        List<KpsModel> models = new ArrayList<>();
        List<BilesikKutukModel> kutukModels = kutukServis.tcKimlikNolariSorgula(link, token, parameters);
        List<AdresModel> adresModels=adresSorgulaServis.adresleriSorgula(link, token, parameters);
        for (BilesikKutukModel kutukModel : kutukModels) {
            KpsModel kpsModel = new KpsModel();
            kpsModel.setKutukModel(kutukModel);
            AdresModel adresModel = adresModels.stream().filter(x->x.getTcKimlikNo().equals(kutukModel.getTcKimlikNo())).findFirst().orElse(null);
            kpsModel.setAdresModel(adresModel);
            models.add(kpsModel);
        }
        return models;
    }


    public List<KpsModel> getKpsFull(String link, String token, KisiParameters parameters) {
        List<KpsModel> models = new ArrayList<>();
//        for (KisiParameter parameter : parameters.getKisiler()) {
//            KpsModel model = new KpsModel();
//            model.setKutukModel(this.kutukServis.tcKimlikNoSorgula(link, token, parameter));
//            model.setAdresModel(this.adresSorgulaServis.adresSorgula(link, token, parameter));
//            model.setKisiAdresModel(this.kisiAdresSorgulaServis.kisiAdresSorgula(link, token, parameter));
//            models.add(model);
//        }

        List<BilesikKutukModel> kutukModels = kutukServis.tcKimlikNolariSorgula(link, token, parameters);
        List<AdresModel> adresModels=adresSorgulaServis.adresleriSorgula(link, token, parameters);
        for (BilesikKutukModel kutukModel : kutukModels) {
            KpsModel kpsModel = new KpsModel();
            kpsModel.setKutukModel(kutukModel);
            AdresModel adresModel = adresModels.stream().filter(x->x.getTcKimlikNo().equals(kutukModel.getTcKimlikNo())).findFirst().orElse(null);
            kpsModel.setAdresModel(adresModel);
//            kpsModel.setKisiAdresModel(this.kisiAdresSorgulaServis.kisiAdresSorgula(link, token, parameter));
            models.add(kpsModel);
        }

        return models;
    }

    public List<KpsModel> getKpsKutukByAdres(String link, String token, KisiParameters parameters) {
        List<KpsModel> models = new ArrayList<>();
        List<BilesikKutukModel> kutukModels = this.kutukServis.tcKimlikNolariSorgula(link, token, parameters);
        List<AdresModel> adresModels = this.adresSorgulaServis.adresleriSorgula(link, token, parameters);
        for (BilesikKutukModel kutukModel : kutukModels) {
            KpsModel model = new KpsModel();
            model.setKutukModel(kutukModel);
            adresModels.stream()
                    .filter(a -> a.getTcKimlikNo().equals(kutukModel.getTcKimlikNo()))
                    .findFirst().ifPresent(model::setAdresModel);
            models.add(model);
        }

        return models;
    }


}
