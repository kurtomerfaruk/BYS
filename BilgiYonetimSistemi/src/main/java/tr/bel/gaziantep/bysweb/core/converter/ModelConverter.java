package tr.bel.gaziantep.bysweb.core.converter;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import tr.bel.gaziantep.bysweb.core.controller.InitApp;
import tr.bel.gaziantep.bysweb.core.enums.bys.EnumModul;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlCinsiyet;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlDurum;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlMedeniDurum;
import tr.bel.gaziantep.bysweb.core.enums.genel.EnumGnlUyruk;
import tr.bel.gaziantep.bysweb.core.utils.DateUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIl;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlIlce;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlMahalle;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlIlceService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlMahalleService;
import tr.bel.gaziantep.bysweb.webservice.gazikart.model.ServisModel;
import tr.bel.gaziantep.bysweb.webservice.kps.controller.KoordinatService;
import tr.bel.gaziantep.bysweb.webservice.kps.model.KpsModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.adres.AdresModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.kisi.KisiAdresModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.koordinat.KoordinatModel;
import tr.bel.gaziantep.bysweb.webservice.kps.model.parameters.KisiParameter;
import tr.bel.gaziantep.bysweb.webservice.kps.util.KpsUtil;

import java.io.Serial;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 19.06.2025 16:39
 */
@Named(value = "modelConverter")
@RequestScoped
public class ModelConverter implements java.io.Serializable {

    @Serial
    private static final long serialVersionUID = -9087671618119837216L;

    @Inject
    private GnlKisiService kisiService;
    @Inject
    private GnlMahalleService mahalleService;
    @Inject
    private GnlIlceService ilceService;
    @Inject
    private GnlIlService sehirService;
    @Inject
    private InitApp initApp;

    private KoordinatService koordinatService = new KoordinatService();

    public GnlKisi convertKpsModelToGnlKisi(GnlKisi kisi, KpsModel kpsModel, EnumModul modul) throws Exception {
        if (kisi == null) kisi = new GnlKisi();
        if (kpsModel.getKutukModel() != null) {
            if(!StringUtil.isBlank(kpsModel.getKutukModel().getUyruk())){
                kisi.setUyruk(EnumGnlUyruk.fromValue(kpsModel.getKutukModel().getUyruk()));
            }

            kisi.setTcKimlikNo(kpsModel.getKutukModel().getTcKimlikNo().toString());
            kisi.setAd(kpsModel.getKutukModel().getAd());
            kisi.setSoyad(kpsModel.getKutukModel().getSoyad());
            kisi.setAnaAdi(kpsModel.getKutukModel().getAnneAd());
            kisi.setBabaAdi(kpsModel.getKutukModel().getBabaAd());
            kisi.setCinsiyet(EnumGnlCinsiyet.fromValue(kpsModel.getKutukModel().getCinsiyetAciklama()));
            kisi.setDogumYeri(kpsModel.getKutukModel().getDogumYeri());
            kisi.setDogumTarihi(kpsModel.getKutukModel().getDogumTarihi());
            kisi.setDurum(EnumGnlDurum.fromValue(kpsModel.getKutukModel().getDurumAciklama()));
            kisi.setMedeniDurum(EnumGnlMedeniDurum.fromValue(kpsModel.getKutukModel().getMedeniHalAciklama()));
            kisi.setOlumTarihi(kpsModel.getKutukModel().getOlumTarihi());
            kisi.setKimlikSeriNo(kpsModel.getKutukModel().getKimlikSeriNo());
            kisi.setSonGecerlilikTarihi(kpsModel.getKutukModel().getSonGecerlilikTarihi());
            kisi.setAktif(true);
            if (kisi.getEklemeYeri() == null) {
                kisi.setEklemeYeri(modul);
            }
            if (kisi.getKayitTarihi() == null) {
                kisi.setKayitTarihi(LocalDate.now());
            }
        }

        if (kpsModel.getAdresModel() != null) {
            AdresModel adresModel = kpsModel.getAdresModel();
            kisi.setAdres(adresModel.getAcikAdres());
            kisi.setAdresNo(adresModel.getAdresNo());

            if (adresModel.getBinaKodu() != null) {
                kisi.setBinaKodu(adresModel.getBinaKodu());
            }
            if (adresModel.getBinaNo() != null) {
                kisi.setBinaNo(adresModel.getBinaNo());

            }
            if (adresModel.getMahalleKodu() != null) {
                GnlMahalle mahalle = mahalleService.findByKod(adresModel.getMahalleKodu());
                if (mahalle != null) {
                    kisi.setGnlMahalle(mahalle);
                }
            }
            if (adresModel.getIlceKodu() != null) {
                GnlIlce ilce = ilceService.findByKod(adresModel.getIlceKodu());
                if (ilce != null) {
                    kisi.setGnlIlce(ilce);
                }
            }
            if (adresModel.getSehirKodu() != null) {
                GnlIl sehir = sehirService.findByKod(adresModel.getSehirKodu());
                if (sehir != null) {
                    kisi.setGnlIl(sehir);
                }
            }

        }

        if (kpsModel.getKisiAdresModel() != null) {
            KisiAdresModel model = kpsModel.getKisiAdresModel();
            if(StringUtil.isNotBlank(model.getX()) && StringUtil.isNotBlank(model.getY())) {
                kisi.setLatLng(model.getX() + "," + model.getY());
            }
            
        }

        kisi.setMernisGuncellemeTarihi(LocalDateTime.now());
        return kisi;
    }

    public String addLatLng(Integer binaNo) {
        if (binaNo != null && binaNo > 0) {
            return findCoordinate(binaNo);
        }
        return null;
    }

    public String findCoordinate(Integer binaNo) {
        KoordinatModel koordinatModel = koordinatService.binaNoIleKoordinatSorgula(initApp.getProperty("webServisLink"),
                initApp.getProperty("webServisToken"), binaNo);
        if (koordinatModel == null) return null;
        if (koordinatModel.getHata() != null) return null;
        if (StringUtil.isBlank(koordinatModel.getLatitude()) && StringUtil.isBlank(koordinatModel.getLongitude()))
            return null;
        if(koordinatModel.getLatitude().equals("null") || koordinatModel.getLongitude().equals("null")) return null;
        return koordinatModel.getLatitude() + "," + koordinatModel.getLongitude();
    }

    public List<KisiParameter> servisModelToKisiParameters(List<ServisModel> models, EnumModul department) {
        List<KisiParameter> result = new ArrayList<>();
        for (ServisModel model : models) {
            if (department.equals(EnumModul.GAZIKART)) {
                if (StringUtil.isBlank(model.getDisabledDegree())) {
                    continue;
                }
                if (result.stream().noneMatch(x -> x.getTcKimlikNo() == Long.parseLong(model.getIdentityNo()))
                        && !model.getDisabledDegree().equals("-")) {
                    LocalDate date = DateUtil.stringToLocalDate(model.getBirthDate(), "dd.MM.yyyy");
                    KisiParameter parameter = KpsUtil.setDate(date);
                    parameter.setTcKimlikNo(Long.parseLong(model.getIdentityNo()));
                    result.add(parameter);
                }

            } else {
                System.out.println("Enum Servis Tur Tanimlanmadi:" + department);
            }
        }
        return result;
    }

    public EyKisi convertKpsModelToEyKisi(KpsModel kpsModel, EnumModul modul) throws Exception {
        EyKisi eyKisi = new EyKisi();
        GnlKisi gnlKisi = kisiService.findByTckimlikNo(kpsModel.getKutukModel().getTcKimlikNo().toString());
        gnlKisi = convertKpsModelToGnlKisi(gnlKisi, kpsModel, modul);
        eyKisi.setGnlKisi(gnlKisi);
        return eyKisi;
    }
}
