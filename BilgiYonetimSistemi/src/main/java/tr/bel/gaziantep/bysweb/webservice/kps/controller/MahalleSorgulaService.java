package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import tr.bel.gaziantep.bysweb.webservice.kps.model.mahalle.MahalleSonucModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.mahalle.MahalleSorguSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KodParameter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 6.08.2025 08:41
 */
public class MahalleSorgulaService extends KpsAbstractService<MahalleSonucModel>{

    @Serial
    private static final long serialVersionUID = -5041874524192784379L;

    public MahalleSorgulaService() {
        super(MahalleSonucModel.class);
    }

    public List<MahalleSorguSonucu> ilceyeBagliMahalleSorgula(String link, String token, KodParameter parameter) {
        return convertMahalleModel(this.postExecute(link + "Kps/KpsIlceyeBagliMahalleSorgula", token, parameter));
    }

    private List<MahalleSorguSonucu> convertMahalleModel(MahalleSonucModel sonucModel) {
        List<MahalleSorguSonucu> models = new ArrayList<>();
        if (sonucModel.getHata() == null) {
            for (MahalleSorguSonucu mahalleSorguSonucu : sonucModel.getSonuc()) {
                MahalleSorguSonucu model = new MahalleSorguSonucu();
                model.setAciklama(mahalleSorguSonucu.getAciklama());
                model.setKod(mahalleSorguSonucu.getKod());
                models.add(model);
            }
        }
        return models;
    }
}
