package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.webservice.kps.model.adres.*;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:31
 */
public class AdresSorgulaService extends KpsAbstractService<AdresSonucModel> {

    @Serial
    private static final long serialVersionUID = 3805460897510401140L;

    public AdresSorgulaService() {
        super(AdresSonucModel.class);
    }

    public AdresModel adresSorgula(String link, String token, KisiParameter parameter) {
        return convertAdresModel(this.postExecute(link + "Kps/KpsTcIleAdresSorgula", token, parameter));
    }

    public List<AdresModel> adresleriSorgula(String link, String token, KisiParameters parameters) {
        return convertAdresModels(this.postExecute(link + "Kps/KpsTcIleAdresSorgula", token, parameters));
    }


    private List<AdresModel> convertAdresModels(AdresSonucModel sonucModel) {
        List<AdresModel> resultList = new ArrayList<>();
        for (AdresSorguSonucu adresSorguSonucu : sonucModel.getSonuc().getSorguSonucu()) {
            AdresModel result = new AdresModel();
            if (adresSorguSonucu.getHataBilgisi() == null) {
                result.setTcKimlikNo(adresSorguSonucu.getKimlikNo());
                YerlesimYeriAdresi adresBilgi = adresSorguSonucu.getYerlesimYeriAdresi();
                result.setAcikAdres(adresBilgi.getAcikAdres());
                result.setAdresNo(adresBilgi.getAdresNo());
                IlIlceMerkezAdresi il = adresBilgi.getIlIlceMerkezAdresi();
                if (il != null) {
                    result.setBinaKodu(il.getBinaKodu());
                    result.setBinaNo(il.getBinaNo());
                    result.setMahalleKodu(il.getMahalleKodu());
                    result.setMahalleAciklama(il.getMahalle());
                    result.setIlceKodu(il.getIlceKodu());
                    result.setIlceAciklama(il.getIlce());
                    result.setDisKapiNo(il.getDisKapiNo());
                    result.setIcKapiNo(il.getIcKapiNo());
                    result.setSehirKodu(il.getIlKodu());
                    result.setSehirAciklama(il.getIl());
                }
            } else {
                result.setTcKimlikNo(adresSorguSonucu.getKimlikNo());
            }
            resultList.add(result);
        }
        return resultList;
    }

    private AdresModel convertAdresModel(AdresSonucModel sonucModel) {
        if (sonucModel == null || sonucModel.getSonuc()==null) return null;
        AdresSorguSonucu bilgi = sonucModel.getSonuc().getSorguSonucu().get(0);
        AdresModel result = new AdresModel();
        if (bilgi.getHataBilgisi() == null) {
            YerlesimYeriAdresi adresBilgi = bilgi.getYerlesimYeriAdresi();
            if(StringUtil.isBlank(adresBilgi.getAcikAdres())){
                DigerAdresBilgileri digerAdres =bilgi.getDigerAdresBilgileri().get(0);
                result.setAcikAdres(digerAdres.getAcikAdres());
                result.setAdresNo(digerAdres.getAdresNo());
                IlIlceMerkezAdresi digerIl = digerAdres.getIlIlceMerkezAdresi();
                if (digerIl != null) {
                    result.setBinaKodu(digerIl.getBinaKodu());
                    result.setBinaNo(digerIl.getBinaNo());
                    result.setMahalleKodu(digerIl.getMahalleKodu());
                    result.setMahalleAciklama(digerIl.getMahalle());
                    result.setIlceKodu(digerIl.getIlceKodu());
                    result.setIlceAciklama(digerIl.getIlce());
                    result.setDisKapiNo(digerIl.getDisKapiNo());
                    result.setIcKapiNo(digerIl.getIcKapiNo());
                    result.setSehirAciklama(digerIl.getIl());
                    result.setSehirKodu(digerIl.getIlKodu());
                }
            }else {
                result.setAcikAdres(adresBilgi.getAcikAdres());
                result.setAdresNo(adresBilgi.getAdresNo());
                IlIlceMerkezAdresi il = adresBilgi.getIlIlceMerkezAdresi();
                if (il != null) {
                    result.setBinaKodu(il.getBinaKodu());
                    result.setBinaNo(il.getBinaNo());
                    result.setMahalleKodu(il.getMahalleKodu());
                    result.setMahalleAciklama(il.getMahalle());
                    result.setIlceKodu(il.getIlceKodu());
                    result.setIlceAciklama(il.getIlce());
                    result.setDisKapiNo(il.getDisKapiNo());
                    result.setIcKapiNo(il.getIcKapiNo());
                    result.setSehirAciklama(il.getIl());
                    result.setSehirKodu(il.getIlKodu());
                }
            }

        }else{
            result.setHataBilgisi(bilgi.getHataBilgisi().getAciklama());
        }
        return result;
    }
}
