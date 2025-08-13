package tr.bel.gaziantep.bysweb.webservice.kps.controller;

import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsTarih;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.BilesikKutukModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.BilesikKutukSonucModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.BilesikKutukSorguSonucu;
import tr.bel.gaziantep.bysweb.webservice.kps.model.bilesikkutuk.KisiBilgisi;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameters;

import java.io.Serial;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:04
 */
public class BilesikKutukSorgulaService extends KpsAbstractService<BilesikKutukSonucModel> {

    @Serial
    private static final long serialVersionUID = 8391908841878718641L;

    public BilesikKutukSorgulaService() {
        super(BilesikKutukSonucModel.class);
    }

    public BilesikKutukModel tcKimlikNoSorgula(String link, String token, KisiParameter parameter) {
        return convertBilesikKutukModel(postExecute(link + "Kps/KpsTcIleKisiSorgula", token, parameter));
    }

    public List<BilesikKutukModel> tcKimlikNolariSorgula(String link, String token, KisiParameters parameters) {
        return convertBilesikKutukModels(this.postExecute(link + "Kps/KpsTcIleKisiSorgula", token, parameters));
    }

    private List<BilesikKutukModel> convertBilesikKutukModels(BilesikKutukSonucModel sonucModel) {
        List<BilesikKutukModel> resultList = new ArrayList<>();
        for (BilesikKutukSorguSonucu sorguSonucu : sonucModel.getSonuc().getSorguSonucu()) {
            BilesikKutukModel result = new BilesikKutukModel();
            if (sorguSonucu.getHataBilgisi() == null) {
                if (sorguSonucu.getKimlikNo().toString().startsWith("9")) {
                    KisiBilgisi kisiBilgi = sorguSonucu.getYabanciKisiKutukleri().getKisiBilgisi();
                    result.setAd(kisiBilgi.getTemelBilgisi().getAd());
                    result.setSoyad(kisiBilgi.getTemelBilgisi().getSoyad());
                    result.setAnneAd(kisiBilgi.getTemelBilgisi().getAnneAd());
                    result.setBabaAd(kisiBilgi.getTemelBilgisi().getBabaAd());
                    result.setDogumTarihi(convertToDate(kisiBilgi.getDogumTarih()));
                    result.setDogumYeri(kisiBilgi.getTemelBilgisi().getDogumYer());
                    result.setDurum(kisiBilgi.getDurumBilgisi().getDurum().getKod());
                    result.setDurumAciklama(kisiBilgi.getDurumBilgisi().getDurum().getAciklama());
                    result.setMedeniHal(kisiBilgi.getDurumBilgisi().getMedeniHal().getKod());
                    result.setMedeniHalAciklama(kisiBilgi.getDurumBilgisi().getMedeniHal().getAciklama());
                    result.setCinsiyet(kisiBilgi.getTemelBilgisi().getCinsiyet().getKod());
                    result.setCinsiyetAciklama(kisiBilgi.getTemelBilgisi().getCinsiyet().getAciklama());
                    KpsTarih olumTarihi = kisiBilgi.getOlumTarih();

                    if (olumTarihi != null && result.getDurum() == 3) {
                        result.setOlumTarihi(convertToDate(olumTarihi));
                    }

                    result.setTcKimlikNo(kisiBilgi.getKimlikNo());
                } else {
                    KisiBilgisi kisiBilgi = sorguSonucu.getTcVatandasiKisiKutukleri().getKisiBilgisi();

                    if (StringUtil.isBlank(kisiBilgi.getTemelBilgisi().getAd())) {
                        kisiBilgi = sorguSonucu.getMaviKartliKisiKutukleri().getKisiBilgisi();
                    }
                    result.setAd(kisiBilgi.getTemelBilgisi().getAd());
                    result.setSoyad(kisiBilgi.getTemelBilgisi().getSoyad());
                    result.setAnneAd(kisiBilgi.getTemelBilgisi().getAnneAd());
                    result.setBabaAd(kisiBilgi.getTemelBilgisi().getBabaAd());
                    result.setDogumTarihi(convertToDate(kisiBilgi.getTemelBilgisi().getDogumTarih()));
                    result.setDogumYeri(kisiBilgi.getTemelBilgisi().getDogumYer());
                    result.setDurum(kisiBilgi.getDurumBilgisi().getDurum().getKod());
                    result.setDurumAciklama(kisiBilgi.getDurumBilgisi().getDurum().getAciklama());
                    result.setMedeniHal(kisiBilgi.getDurumBilgisi().getMedeniHal().getKod());
                    result.setMedeniHalAciklama(kisiBilgi.getDurumBilgisi().getMedeniHal().getAciklama());
                    result.setCinsiyet(kisiBilgi.getTemelBilgisi().getCinsiyet().getKod());
                    result.setCinsiyetAciklama(kisiBilgi.getTemelBilgisi().getCinsiyet().getAciklama());
                    KpsTarih olumTarihi = kisiBilgi.getDurumBilgisi().getOlumTarih();

                    if (olumTarihi != null && result.getDurum() == 3) {
                        result.setOlumTarihi(convertToDate(olumTarihi));
                    }

                    result.setTcKimlikNo(kisiBilgi.getTcKimlikNo());
                }
            }
            resultList.add(result);
        }
        return resultList;
    }

    private BilesikKutukModel convertBilesikKutukModel(BilesikKutukSonucModel sonucModel) {
        if (sonucModel == null) return null;
        BilesikKutukModel result = new BilesikKutukModel();
        if (sonucModel.getHata() != null) {
            result.setHataBilgisi(sonucModel.getHata().getHataMesaji());
            return result;
        }
        BilesikKutukSorguSonucu bilgi = sonucModel.getSonuc().getSorguSonucu().get(0);

        if (bilgi.getHataBilgisi() == null) {
            if (bilgi.getKimlikNo().toString().startsWith("9")) {
                KisiBilgisi kisiBilgi = bilgi.getYabanciKisiKutukleri().getKisiBilgisi();
                result.setAd(kisiBilgi.getTemelBilgisi().getAd());
                result.setSoyad(kisiBilgi.getTemelBilgisi().getSoyad());
                result.setAnneAd(kisiBilgi.getTemelBilgisi().getAnneAd());
                result.setBabaAd(kisiBilgi.getTemelBilgisi().getBabaAd());
                result.setDogumTarihi(convertToDate(kisiBilgi.getDogumTarih()));
                result.setDogumYeri(kisiBilgi.getTemelBilgisi().getDogumYer());
                result.setDurum(kisiBilgi.getDurumBilgisi().getDurum().getKod());
                result.setDurumAciklama(kisiBilgi.getDurumBilgisi().getDurum().getAciklama());
                result.setMedeniHal(kisiBilgi.getDurumBilgisi().getMedeniHal().getKod());
                result.setMedeniHalAciklama(kisiBilgi.getDurumBilgisi().getMedeniHal().getAciklama());
                result.setCinsiyet(kisiBilgi.getTemelBilgisi().getCinsiyet().getKod());
                result.setCinsiyetAciklama(kisiBilgi.getTemelBilgisi().getCinsiyet().getAciklama());
                KpsTarih olumTarihi = kisiBilgi.getOlumTarih();

                if (olumTarihi != null && result.getDurum() == 3) {
                    result.setOlumTarihi(convertToDate(olumTarihi));
                }

                result.setTcKimlikNo(kisiBilgi.getKimlikNo());
            } else {
                KisiBilgisi kisiBilgi = bilgi.getTcVatandasiKisiKutukleri().getKisiBilgisi();
                if (StringUtil.isBlank(kisiBilgi.getTemelBilgisi().getAd())) {
                    kisiBilgi = bilgi.getMaviKartliKisiKutukleri().getKisiBilgisi();
                    result.setKimlikSeriNo(bilgi.getMaviKartliKisiKutukleri().getYeniMaviKartBilgisi().getSeriNo());
                    result.setSonGecerlilikTarihi(convertToDate(bilgi.getMaviKartliKisiKutukleri().getYeniMaviKartBilgisi().getSonGecerlilikTarih()));
                    result.setUyruk(bilgi.getMaviKartliKisiKutukleri().getYeniMaviKartBilgisi().getUyruk().getAciklama());
                    result.setTcKimlikNo(kisiBilgi.getKimlikNo());
                }else{
                    result.setUyruk("Türkiye Cumhuriyeti");
                    result.setKimlikSeriNo(bilgi.getTcVatandasiKisiKutukleri().getTCKKBilgisi().getSeriNo());
                    result.setSonGecerlilikTarihi(convertToDate(bilgi.getTcVatandasiKisiKutukleri().getTCKKBilgisi().getSonGecerlilikTarih()));
                    result.setTcKimlikNo(kisiBilgi.getTcKimlikNo());
                }
                result.setAd(kisiBilgi.getTemelBilgisi().getAd());
                result.setSoyad(kisiBilgi.getTemelBilgisi().getSoyad());
                result.setAnneAd(kisiBilgi.getTemelBilgisi().getAnneAd());
                result.setBabaAd(kisiBilgi.getTemelBilgisi().getBabaAd());
                result.setDogumTarihi(convertToDate(kisiBilgi.getTemelBilgisi().getDogumTarih()));
                result.setDogumYeri(kisiBilgi.getTemelBilgisi().getDogumYer());
                result.setDurum(kisiBilgi.getDurumBilgisi().getDurum().getKod());
                result.setDurumAciklama(kisiBilgi.getDurumBilgisi().getDurum().getAciklama());
                result.setMedeniHal(kisiBilgi.getDurumBilgisi().getMedeniHal().getKod());
                result.setMedeniHalAciklama(kisiBilgi.getDurumBilgisi().getMedeniHal().getAciklama());
                result.setCinsiyet(kisiBilgi.getTemelBilgisi().getCinsiyet().getKod());
                result.setCinsiyetAciklama(kisiBilgi.getTemelBilgisi().getCinsiyet().getAciklama());
                KpsTarih olumTarihi = kisiBilgi.getDurumBilgisi().getOlumTarih();

                if (olumTarihi != null && result.getDurum() == 3) {
                    result.setOlumTarihi(convertToDate(olumTarihi));
                }


            }
        } else {
            result.setHataBilgisi(bilgi.getHataBilgisi().getAciklama());
        }
        return result;
    }

    private static LocalDate convertToDate(KpsTarih value) {
        LocalDate date = LocalDate.now();
        if (value != null) {
            if (value.getYil() != null) {
                date = date.withYear(value.getYil());
            }
            if (value.getAy() != null) {
                date = date.withMonth(value.getAy());
            } else {
                date = date.withMonth(Month.JANUARY.getValue());
            }
            if (value.getGun() != null) {
                date = date.withDayOfMonth(value.getGun());
            } else {
                date = date.withDayOfMonth(1);
            }
        }
        return date;
    }
}
