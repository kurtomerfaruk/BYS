package tr.bel.gaziantep.bysweb.moduls.genel.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import tr.bel.gaziantep.bysweb.core.controller.AbstractController;
import tr.bel.gaziantep.bysweb.core.utils.FacesUtil;
import tr.bel.gaziantep.bysweb.core.utils.StringUtil;
import tr.bel.gaziantep.bysweb.core.utils.Util;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyEtkinlikKisi;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.entity.AyGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyEtkinlikKisiService;
import tr.bel.gaziantep.bysweb.moduls.aktifyasam.service.AyGirisCikisService;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmGirisCikis;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.entity.EkmIsBasvuru;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmGirisCikisService;
import tr.bel.gaziantep.bysweb.moduls.engelsizkariyermerkezi.service.EkmIsBasvuruService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracCihazTeslimi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyAracTamir;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EyKisi;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.entity.EySosyalIncelemeRaporu;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracCihazTeslimiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyAracTamirService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EyKisiService;
import tr.bel.gaziantep.bysweb.moduls.engelsizler.service.EySosyalIncelemeRaporuService;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisi;
import tr.bel.gaziantep.bysweb.moduls.genel.entity.GnlKisiInceleme;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiIncelemeService;
import tr.bel.gaziantep.bysweb.moduls.genel.service.GnlKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeEtkinlikKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeKisi;
import tr.bel.gaziantep.bysweb.moduls.moralevi.entity.MeTalep;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeEtkinlikKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeKisiService;
import tr.bel.gaziantep.bysweb.moduls.moralevi.service.MeTalepService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKanTahlilSonuc;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.entity.ShKisiKontrol;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiKanTahlilSonucService;
import tr.bel.gaziantep.bysweb.moduls.saglikhizmetleri.service.ShKisiKontrolService;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Omer Faruk KURT kurtomerfaruk@gmail.com
 * @version 1.0.0
 * @since 15.08.2025 14:29
 */
@Named
@RequestScoped
@Slf4j
public class GnlKisiIncelemeController extends AbstractController<GnlKisiInceleme> {

    @Serial
    private static final long serialVersionUID = 2998639509131273143L;

    @Inject
    private GnlKisiService service;
    @Inject
    private GnlKisiIncelemeService gnlKisiIncelemeService;
    @Inject
    private AyEtkinlikKisiService ayEtkinlikKisiService;
    @Inject
    private AyGirisCikisService ayGirisCikisService;
    @Inject
    private EkmIsBasvuruService ekmIsBasvuruService;
    @Inject
    private EkmGirisCikisService ekmGirisCikisService;
    @Inject
    private EyKisiService eyKisiService;
    @Inject
    private EyAracCihazTeslimiService eyAracCihazTeslimleriService;
    @Inject
    private EyAracTamirService eyAracTamirleriService;
    @Inject
    private EySosyalIncelemeRaporuService eySosyalIncelemeRaporuService;
    @Inject
    private MeKisiService meKisiService;
    @Inject
    private MeEtkinlikKisiService meEtkinlikKisiService;
    @Inject
    private MeTalepService meTalepService;
    @Inject
    private ShKisiKontrolService shKisiKontrolService;
    @Inject
    private ShKisiKanTahlilSonucService shKisiKanTahlilSonucService;

    @Getter
    @Setter
    private GnlKisiInceleme gnlKisiInceleme;
    @Getter
    @Setter
    private GnlKisi gnlKisi;
    @Getter
    @Setter
    private boolean aktifYasam;
    @Getter
    @Setter
    private List<AyEtkinlikKisi> ayEtkinlikKisiList;
    @Getter
    @Setter
    private List<AyGirisCikis> ayGirisCikisList;
    @Getter
    @Setter
    private boolean engelliKoordinasyonMerkezi;
    @Getter
    @Setter
    private List<EkmIsBasvuru> ekmIsBasvuruList;
    @Getter
    @Setter
    private List<EkmGirisCikis> ekmGirisCikisList;
    @Getter
    @Setter
    private boolean engelsiz;
    @Getter
    @Setter
    private List<EyKisi> eyKisiList;
    @Getter
    @Setter
    private List<EyAracCihazTeslimi> eyAracCihazTeslimleriList;
    @Getter
    @Setter
    private List<EyAracTamir> eyAracTamirleriList;
    @Getter
    @Setter
    private List<EySosyalIncelemeRaporu> eySosyalIncelemeRaporuList;
    @Getter
    @Setter
    private boolean moralEvi;
    @Getter
    @Setter
    private List<MeKisi> meKisiList;
    @Getter
    @Setter
    private List<MeEtkinlikKisi> meEtkinlikKisiList;
    @Getter
    @Setter
    private List<MeTalep> meTalepList;
    @Getter
    @Setter
    private boolean saglikHizmetleri;
    @Getter
    @Setter
    private List<ShKisiKanTahlilSonuc> shKanTahlilSonucList;
    @Getter
    @Setter
    private List<ShKisiKontrol> shKisiKontrolList;


    public GnlKisiIncelemeController() {
        super(GnlKisiInceleme.class);
        clearSelect();
    }

    public void clearSelect() {
        gnlKisi = GnlKisi.builder().build();
        gnlKisiInceleme = GnlKisiInceleme.builder().build();
        ayEtkinlikKisiList = new ArrayList<>();
        ayGirisCikisList = new ArrayList<>();
        ekmIsBasvuruList = new ArrayList<>();
        ekmGirisCikisList = new ArrayList<>();
        eyKisiList = new ArrayList<>();
        eyAracCihazTeslimleriList = new ArrayList<>();
        eyAracTamirleriList = new ArrayList<>();
        eySosyalIncelemeRaporuList = new ArrayList<>();
        meKisiList = new ArrayList<>();
        meEtkinlikKisiList = new ArrayList<>();
        meTalepList = new ArrayList<>();
        shKanTahlilSonucList = new ArrayList<>();
        shKisiKontrolList = new ArrayList<>();
    }

    public void getTcKimlik() {
        if (gnlKisi != null) {
            try {
                if (StringUtil.isBlank(gnlKisi.getTcKimlikNo())) {
                    FacesUtil.errorMessage("tcNoBos");
                    return;
                }
                if (StringUtil.isBlank(gnlKisiInceleme.getSorgulamaGerekcesi())) {
                    FacesUtil.warningMessage("gerekceBos");
                    return;
                }

                GnlKisi kisi = service.findByTckimlikNo(gnlKisi.getTcKimlikNo());
                if (kisi != null) {
                    this.setGnlKisi(kisi);
                    fetchAktifYasam(kisi);
                    fetchEngelliKoordinasyonMerkezi(kisi);
                    fetchEngelsiz(kisi);
                    fetchMoralEvi(kisi);
                    fetchSaglikHizmetleri(kisi);
                    gnlKisiInceleme.setSorgulananGnlKisi(kisi);
                    gnlKisiInceleme.setSorgulayanGnlKisi(Util.getSyKullanici().getGnlPersonel().getGnlKisi());
                    gnlKisiIncelemeService.create(gnlKisiInceleme);
                } else {
                    FacesUtil.warningMessage("tcKimlikBulunamadi");
                }
            } catch (Exception e) {
                log.error(null, e);
            }
        }
    }

    private void fetchAktifYasam(GnlKisi kisi) {
        ayEtkinlikKisiList = ayEtkinlikKisiService.findByGnlKisi(kisi);
        ayGirisCikisList = ayGirisCikisService.findByGnlKisi(kisi);
        aktifYasam = !ayEtkinlikKisiList.isEmpty() || !ayGirisCikisList.isEmpty();
    }

    private void fetchEngelliKoordinasyonMerkezi(GnlKisi kisi) {
        ekmIsBasvuruList = ekmIsBasvuruService.findByGnlKisi(kisi);
        ekmGirisCikisList = ekmGirisCikisService.findByGnlKisi(kisi);
        engelliKoordinasyonMerkezi = !ekmIsBasvuruList.isEmpty() || !ekmGirisCikisList.isEmpty();
    }

    private void fetchEngelsiz(GnlKisi kisi) {
        eyKisiList = eyKisiService.findByKisiList(kisi);
        eyAracCihazTeslimleriList = eyAracCihazTeslimleriService.findByGnlKisi(kisi);
        eyAracTamirleriList = eyAracTamirleriService.findByGnlKisi(kisi);
        eySosyalIncelemeRaporuList = eySosyalIncelemeRaporuService.findByGnlKisi(kisi);
        engelsiz = !eyKisiList.isEmpty() || !eyAracCihazTeslimleriList.isEmpty() || !eyAracTamirleriList.isEmpty() || !eySosyalIncelemeRaporuList.isEmpty();

    }


    private void fetchMoralEvi(GnlKisi kisi) {
        meKisiList = meKisiService.findByGnlKisi(kisi);
        meEtkinlikKisiList = meEtkinlikKisiService.findByGnlKisi(kisi);
        meTalepList = meTalepService.findByGnlKisi(kisi);
        moralEvi = !meKisiList.isEmpty() || !meEtkinlikKisiList.isEmpty() || !meTalepList.isEmpty();
    }

    private void fetchSaglikHizmetleri(GnlKisi kisi) {
        shKanTahlilSonucList = shKisiKanTahlilSonucService.findByGnlKisi(kisi);
        shKisiKontrolList = shKisiKontrolService.findByGnlKisi(kisi);
        saglikHizmetleri = !shKanTahlilSonucList.isEmpty() || !shKisiKontrolList.isEmpty();
    }


}
